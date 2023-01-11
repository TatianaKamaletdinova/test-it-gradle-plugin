package ru.kamal.testit.util

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import java.io.File

/**
 * Интерфейс, использующийся в MockDispatcher для получения моковых ответов
 */
interface ResponseProvider {
    fun provideResponse(request: RecordedRequest): MockResponse

    fun addResponse(
        urlPath: String,
        code: Int,
        body: String,
        headers: Map<String, String> = emptyMap(),
        bodyDelay: Long = 0
    )

    fun addFileResponse(
        urlPath: String,
        code: Int,
        file: File,
        headers: Map<String, String> = emptyMap(),
        bodyDelay: Long = 0
    )
}