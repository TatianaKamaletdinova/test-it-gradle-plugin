package ru.kamal.testit.util

import okhttp3.internal.wait
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import okio.buffer
import okio.source
import java.io.File
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

/**
 * Провайдер, используемый в MockDispatcher по-умолчанию
 */
class DefaultResponseProvider : ResponseProvider {

    /**
     * Хранит моковые ответы сервера в виде ключ-значение, где ключ - это путь реквеста (без ендпоинта), например, /auth/bylogin,
     * а значение - это модель MockResponse, хранящая в себе данные ответа (код, тело и хедеры)
     */
    private val responses = ConcurrentHashMap<String, MockResponse>()

    override fun provideResponse(request: RecordedRequest): MockResponse {
        //сортируем по длине ключа, чтобы более длинные запросы с одинаковым префиксом не игнорировались
        responses.toList().sortedByDescending { (key, _) -> key.length }
            .forEach { (key, value) ->
            // В api ретрофита можно писать "/" в начале, а можно и не писать, поэтому мы всегда удаляем "/" в начале,
            // чтобы не было ошибок
            if (request.getUrlPath()?.startsWith(key) == true) return value
        }

        // Блокируем тред запроса если не нашли mock для этого реквеста
        synchronized(this) { wait() }
        // wait() не может быть последним методом, поэтому кидаем эксепшн который никогда не вызовется
        throw NullPointerException()
    }

    override fun addResponse(
        urlPath: String,
        code: Int,
        body: String,
        headers: Map<String, String>,
        bodyDelay: Long
    ) {
        // В api ретрофита можно писать "/" в начале, а можно и не писать, поэтому мы всегда удаляем "/" в начале,
        // чтобы не было ошибок
        responses[urlPath.removePrefix("/")] = MockResponse()
            .apply {
                headers.forEach { (key, value) -> addHeader(key, value) }
            }
            .setResponseCode(code)
            .setBody(body)
            .setBodyDelay(bodyDelay, TimeUnit.MILLISECONDS)
    }

    override fun addFileResponse(urlPath: String, code: Int, file: File, headers: Map<String, String>, bodyDelay: Long) {
        responses[urlPath.removePrefix("/")] = MockResponse()
            .apply {
                headers.forEach { (key, value) -> addHeader(key, value) }
            }
            .setResponseCode(code)
            .setBody(file.source().buffer().buffer)
            .setBodyDelay(bodyDelay, TimeUnit.MILLISECONDS)
    }
}