package ru.kamal.testit.plugin

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import ru.kamal.testit.plugin.data.model.AllureLink
import ru.kamal.testit.plugin.data.model.AllureReport
import ru.kamal.testit.plugin.data.model.AttachmentId
import ru.kamal.testit.plugin.data.model.body.TestResultsBody
import ru.kamal.testit.plugin.data.model.mapToTestResultsBody
import ru.kamal.testit.plugin.data.model.responce.ResponseProjectDto
import ru.kamal.testit.plugin.data.repo.TestITRepository
import ru.kamal.testit.plugin.util.parser.AllureParser
import java.io.File

class SendManager(
    testITUrl: String,
    projectId: String,
    namespace: String,
    private val configurationId: String,
    private val inputDir: File,
    private val testRunName: String,
    private val privateToken: String
) {

    private val testITRepository = TestITRepository(testITUrl, projectId, namespace, privateToken)

    private val allureParser = AllureParser()

    suspend fun getProject(): List<ResponseProjectDto?>? {
        return testITRepository.getProject()
    }

    suspend fun initWorkTestIT() = withContext(Dispatchers.Default) {
        val files = inputDir.listFiles()

        val newReports = withContext(Dispatchers.Default) {
            val newReports = mutableListOf<AllureReport>()

            files?.filter { it.extension == "json" }?.forEach { file ->
                val rawReport = allureParser.parse(file.bufferedReader().readText())
                    ?: throw NullPointerException("json is empty")
                val newReport = withContext(Dispatchers.Default) {
                    createOrUpdateAutoTestWithLink(rawReport, files)
                }
                newReports.add(newReport)
            }

            newReports
        }

       val testRunId = async { createNewTestRun(testRunName) }
        createTestResults(testRunId.await(), newReports.map { it.mapToTestResultsBody(configurationId) })
    }

    private suspend fun createOrUpdateAutoTestWithLink(
        rawReport: AllureReport,
        files: Array<File>?
    ): AllureReport {
        println("textReport $rawReport")

        val externalId = rawReport.historyId ?: throw NullPointerException("json is empty")

        val tms = rawReport.links?.find { it?.type == "tms" }
            ?: throw NullPointerException("links is empty")

        return try {
            val testCase = testITRepository.getWorkItemsById(tms.name ?: throw NullPointerException("name is empty"))
            val testName = testCase?.name ?: rawReport.name ?: "autotest"

            val extId = testCase?.autoTests?.firstOrNull()?.externalId

            if (extId == null) {
                testITRepository.createAutoTestAndLink(
                    externalId = externalId,
                    idTest = tms.name,
                    name = testName,
                    url = tms.url ?: ""
                )
            } else {
                testITRepository.updateAutoTest(
                    externalId = extId,
                    idTest = tms.name,
                    name = testName,
                    url = tms.url ?: ""
                )
            }

            val attachmentIds = mutableListOf<AttachmentId>()
            rawReport.attachments?.forEach { attachment ->
                val fileName = attachment?.source ?: ""
                val file = files?.find { it.name == fileName }
                file?.let {
                    val id = sendAttachment(it)
                    attachmentIds.add(AttachmentId(id))
                }
            }

            rawReport.copy(
                attachmentIds = attachmentIds,
                links = rawReport.links.filter { it?.type == "tms" }.mapNotNull {
                    AllureLink(
                        name = it?.name ?: return@mapNotNull null,
                        type = "Related",
                        url = it.url
                    )
                }
            )

        } catch (error: Exception) {
            println(error.stackTrace)
            throw error
        }
    }

    private suspend fun sendAttachment(file: File) = testITRepository.attachments(file)

    private suspend fun createNewTestRun(testRunName: String): String =
        testITRepository.testRuns(testRunName)

    private suspend fun createTestResults(
        testRunId: String,
        testResultsBody: List<TestResultsBody>
    ) {
        testITRepository.testResults(testRunId = testRunId, testResultsBody)
    }
}