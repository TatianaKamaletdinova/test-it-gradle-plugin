package ru.kamal.testit.plugin.data.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import ru.kamal.testit.plugin.data.model.body.CreateAutoTestBody
import ru.kamal.testit.plugin.data.model.body.LinkAutoTestBody
import ru.kamal.testit.plugin.data.model.body.TestResultsBody
import ru.kamal.testit.plugin.data.model.body.TestRunBody
import ru.kamal.testit.plugin.data.model.responce.GetAutoTestDtoItem
import ru.kamal.testit.plugin.data.model.responce.ResponseProjectDto
import ru.kamal.testit.plugin.data.model.responce.WorkItemsDto
import ru.kamal.testit.plugin.data.network.NetworkConfiguration
import ru.kamal.testit.plugin.data.network.TestITApi
import java.io.File
import java.nio.file.Files
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TestITRepository(
    testITUrl: String,
    private val projectId: String,
    private val namespace: String?,
    privateToken: String
) {

    companion object {
        private const val NAME_KEY = "file"
    }

    private val testIT: TestITApi =
        NetworkConfiguration(testITUrl, privateToken).provideTestITApi()

    private val labels = listOf(
        CreateAutoTestBody.LabelsAutoTest(
            name = "severity",
            value = "critical",
        )
    )

    suspend fun getWorkItemsById(globalId: String): WorkItemsDto? {
        return testIT.getWorkItemsById(globalId)
    }

    suspend fun getAutoTest(externalId: String): List<GetAutoTestDtoItem?>? {
        return testIT.getAutoTest(projectId, externalId)?.mapNotNull { it }
    }

    suspend fun updateAutoTest(
        externalId: String,
        idTest: String,
        name: String,
        url: String,
    ) {
        val response = testIT.updateAutoTest(
            CreateAutoTestBody(
                shouldCreateWorkItem = false,
                externalId = externalId,
                links = listOf(
                    CreateAutoTestBody.LinksAutoTest(
                        title = idTest,
                        url = url,
                        type = "Related",
                        hasInfo = true
                    )
                ),
                projectId = projectId,
                name = name,
                namespace = namespace ?: "namespace",
                isFlaky = false,
                labels = labels
            )
        )
        if (response.isSuccessful.not()) throw HttpException(response)
    }

    suspend fun createAutoTestAndLink(
        externalId: String,
        idTest: String,
        name: String,
        url: String,
    ) {
        val result = testIT.createAutoTest(
            CreateAutoTestBody(
                shouldCreateWorkItem = false,
                externalId = externalId,
                links = listOf(
                    CreateAutoTestBody.LinksAutoTest(
                        title = idTest,
                        url = url,
                        type = "Related",
                        hasInfo = true
                    )
                ),
                projectId = projectId,
                name = name,
                namespace = namespace ?: "namespace",
                isFlaky = false,
                labels = labels
            )
        )

        result.globalId?.let {
            val response = testIT.linkAutoTest(globalId = it, LinkAutoTestBody(idTest))
            if (response.isSuccessful.not()) throw HttpException(response)
        }
    }

    suspend fun testResults(testRunId: String, testResultsBody: List<TestResultsBody>) {
        testIT.testResults(
            testRunId = testRunId,
            testResultsBody = testResultsBody
        )
    }

    suspend fun getProject(): List<ResponseProjectDto?>? {
        return testIT.getProject()
    }

    suspend fun testRuns(testRunName: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val current = LocalDateTime.now().format(formatter)
        return testIT.testRuns(TestRunBody(projectId = projectId, "$testRunName $current")).id
            ?: throw NullPointerException("idTestRun is null")
    }

    suspend fun attachments(file: File): String {
        val mimeType = withContext(Dispatchers.IO) {
            Files.probeContentType(file.toPath())
        }
        val multipartBody = MultipartBody.Part.createFormData(
            name = NAME_KEY,
            filename = file.name,
            body = file.asRequestBody(mimeType.toMediaTypeOrNull())
        )

        return testIT.attachments(multipartBody).id
            ?: throw NullPointerException("idAttachments is null")
    }
}