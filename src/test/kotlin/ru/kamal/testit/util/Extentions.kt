package ru.kamal.testit.util

import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import okio.buffer
import okio.source

@Throws(NullPointerException::class)
fun <T> Class<T>.getJsonFromFile(path: String): String {
    val stream = classLoader?.getResourceAsStream(path)
    val file = stream?.source()?.buffer()?.use {
        it.readString(Charsets.UTF_8)
    }
    return file ?: throw NullPointerException("Не удалось найти файл $path")
}

/**
 * @param bodyDelay - имитация задержки ответа (обычно нужно чтобы проверить лоадер с загрузкой)
 */
fun MockWebServer.addResponseJson(
    urlPath: String,
    code: Int,
    jsonPath: String,
    headers: Map<String, String> = emptyMap(),
    bodyDelay: Long = 0
) {
    addResponse(urlPath, code, javaClass.getJsonFromFile(jsonPath), headers, bodyDelay)
}

fun MockWebServer.addResponse(
    urlPath: String,
    code: Int,
    body: String,
    headers: Map<String, String> = emptyMap(),
    bodyDelay: Long = 0
) {
    if (this.dispatcher !is MockDispatcher) this.dispatcher = MockDispatcher()
    (this.dispatcher as? MockDispatcher)?.addResponse(urlPath, code, body, headers, bodyDelay)
}

fun RecordedRequest.getUrlPath(): String? = path?.removePrefix("/")

fun MockWebServer.baseUrl(): String = this.url("/").toString()