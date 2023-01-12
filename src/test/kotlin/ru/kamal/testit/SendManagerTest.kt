package ru.kamal.testit

import kotlinx.coroutines.runBlocking
import okhttp3.internal.closeQuietly
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import ru.kamal.testit.plugin.SendManager
import ru.kamal.testit.plugin.data.network.TestITApi
import ru.kamal.testit.util.addResponseJson
import ru.kamal.testit.util.baseUrl
import java.io.File

class SendManagerTest {

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start(8080)
    }

    @Test
    fun `создание автотеста, линкование его к тест-кейсу, создание тест-рана и загрузка в него результата`() {
        val generatedFile =
            File("/Users/tkamaletdinova/Desktop/test-it-gradle-plugin/src/test/resources/allure-result")
        assert(generatedFile.exists())

        runBlocking {
            mockWebServer.addResponseJson(
                urlPath = "${TestITApi.AUTO_TEST}?projectId=projectId&externalId=1e9e8128599ca6f7c2ac1142e7f3070e",
                code = 200,
                jsonPath = "getAutoTest.json"
            )

            mockWebServer.addResponseJson(
                urlPath = TestITApi.AUTO_TEST,
                code = 200,
                jsonPath = "createAutoTest.json"
            )

            mockWebServer.addResponseJson(
                urlPath = TestITApi.ATTACHMENTS,
                code = 200,
                jsonPath = "attachments.json"
            )

            mockWebServer.addResponseJson(
                urlPath = TestITApi.TEST_RUN,
                code = 200,
                jsonPath = "testRuns.json"
            )

            mockWebServer.addResponseJson(
                urlPath = TestITApi.TEST_RESULT,
                code = 204,
                jsonPath = "testResults.json"
            )

            try {
                SendManager(
                    testITUrl = mockWebServer.baseUrl(),
                    projectId = "projectId",
                    namespace = "namespace",
                    inputDir = generatedFile,
                    configurationId = "configurationId",
                    testRunName = "testRunName"
                ).initWorkTestIT()
            } catch (e: Exception) {
                throw e
            }
        }
    }

    @Test
    fun `обновление автотеста, линкование его к тест-кейсу, создание тест-рана и загрузка в него результата`() {
        val generatedFile =
            File("/Users/tkamaletdinova/Desktop/test-it-gradle-plugin/src/test/resources/allure-result")
        assert(generatedFile.exists())

        runBlocking {
            mockWebServer.addResponseJson(
                urlPath = "${TestITApi.AUTO_TEST}?projectId=projectId&externalId=1e9e8128599ca6f7c2ac1142e7f3070e",
                code = 200,
                jsonPath = "getExistAutoTest.json"
            )

            mockWebServer.addResponseJson(
                urlPath = TestITApi.AUTO_TEST,
                code = 200,
                jsonPath = "createAutoTest.json"
            )

            mockWebServer.addResponseJson(
                urlPath = TestITApi.ATTACHMENTS,
                code = 200,
                jsonPath = "attachments.json"
            )

            mockWebServer.addResponseJson(
                urlPath = TestITApi.TEST_RUN,
                code = 200,
                jsonPath = "testRuns.json"
            )

            mockWebServer.addResponseJson(
                urlPath = TestITApi.TEST_RESULT,
                code = 204,
                jsonPath = "testResults.json"
            )

            try {
                SendManager(
                    testITUrl = mockWebServer.baseUrl(),
                    projectId = "projectId",
                    namespace = "namespace",
                    inputDir = generatedFile,
                    configurationId = "configurationId",
                    testRunName = "testRunName",
                ).initWorkTestIT()
            } catch (e: Exception) {
                throw e
            }
        }
    }

    @After
    fun cleanUp() {
        // уничтожаем моковский сервер
        // Используем closeQuietly чтобы тест не падал если есть реквесты, которые не были замоканы
        mockWebServer.closeQuietly()
    }
}