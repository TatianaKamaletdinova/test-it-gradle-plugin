package ru.kamal.testit.plugin.data.network

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*
import ru.kamal.testit.plugin.data.model.body.CreateAutoTestBody
import ru.kamal.testit.plugin.data.model.body.LinkAutoTestBody
import ru.kamal.testit.plugin.data.model.body.TestResultsBody
import ru.kamal.testit.plugin.data.model.body.TestRunBody
import ru.kamal.testit.plugin.data.model.responce.*

interface TestITApi {

    companion object {
        const val AUTO_TEST = "autoTests"
        const val LINK_AUTO_TEST = "autoTests/{globalId}/workItems"
        const val TEST_RESULT = "testRuns/{testRunId}/testResults"
        const val TEST_RUN = "testRuns"
        const val PROJECT = "projects"
        const val ATTACHMENTS = "Attachments"
        const val WORK_ITEMS = "workItems/{globalId}"
    }

    /*Создание автотеста*/
    @Headers(
        "Content-Type: application/json-patch+json",
        "accept: application/json"
    )
    @POST(AUTO_TEST)
    suspend fun createAutoTest(@Body createAutoTestBody: CreateAutoTestBody): ResponseCreatedAutoTestDto

    @Headers(
        "Content-Type: application/json-patch+json",
        "accept: application/json"
    )
    @GET(AUTO_TEST)
    suspend fun getAutoTest(
        @Query("projectId") projectId: String,
        @Query("externalId") externalId: String,
    ): List<GetAutoTestDtoItem?>?

    @Headers(
        "Content-Type: application/json-patch+json",
        "accept: application/json"
    )
    @PUT(AUTO_TEST)
    suspend fun updateAutoTest(@Body createAutoTestBody: CreateAutoTestBody): Response<Unit>

    /* Создание ссылки у тест-кейса на автотест по его globalId */
    @Headers(
        "Content-Type: application/json-patch+json",
        "accept: application/json"
    )
    @POST(LINK_AUTO_TEST)
    suspend fun linkAutoTest(
        @Path("globalId") globalId: Int,
        @Body linkAutoTestBody: LinkAutoTestBody
    ): Response<Unit>

    /* Создание результатов автотестов */
    @Headers(
        "Content-Type: application/json-patch+json",
        "accept: application/json"
    )
    @POST(TEST_RESULT)
    suspend fun testResults(
        @Path("testRunId") testRunId: String,
        @Body testResultsBody: List<TestResultsBody>
    )

    @Headers("accept: application/json")
    @GET(PROJECT)
    suspend fun getProject(): List<ResponseProjectDto?>?

    /* Создание TestRun авто-естов */
    @Headers(
        "Content-Type: application/json-patch+json",
        "accept: application/json"
    )
    @POST(TEST_RUN)
    suspend fun testRuns(@Body testRunBody: TestRunBody): TestRunDto

    /* Отправление вложения */
    @Multipart
    @POST(ATTACHMENTS)
    suspend fun attachments(@Part file: MultipartBody.Part): TestRunDto


    @Headers("accept: application/json")
    @GET(WORK_ITEMS)
    suspend fun getWorkItemsById(@Path("globalId") globalId: String): WorkItemsDto?
}