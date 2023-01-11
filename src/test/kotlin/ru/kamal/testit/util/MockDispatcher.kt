package ru.kamal.testit.util

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import java.io.File

/**
 * Диспатчер, используемый в MockWebServer
 */
class MockDispatcher : Dispatcher() {

    /**
     * Провайдер для обработки запросов
     * Чтобы переопределить логику обработки mock запросов, необходимо засетить свой провайдер,
     * имплементирующий интерфейс ResponseProvider
     */
    var responseProvider: ResponseProvider = DefaultResponseProvider()

    override fun dispatch(request: RecordedRequest): MockResponse {
        return responseProvider.provideResponse(request)
    }

    fun addResponse(
        urlPath: String,
        code: Int,
        body: String,
        headers: Map<String, String> = emptyMap(),
        bodyDelay: Long = 0
    ) {
        responseProvider.addResponse(urlPath, code, body, headers, bodyDelay)
    }

    fun addFileResponse(
        urlPath: String,
        code: Int,
        file: File,
        headers: Map<String, String> = emptyMap(),
        bodyDelay: Long = 0
    ) {
        responseProvider.addFileResponse(urlPath, code, file, headers, bodyDelay)
    }

    companion object {
        const val LONG_TIME_OUT = 12_000L
    }
}
