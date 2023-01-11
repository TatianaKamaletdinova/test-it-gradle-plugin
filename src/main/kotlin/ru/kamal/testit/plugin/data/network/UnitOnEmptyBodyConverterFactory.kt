package ru.kamal.testit.plugin.data.network

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * Нужен чтобы не возникала ошибка когде сервер возвращает пустое тело в ответе (а должен возвращать {})
 * https://github.com/square/retrofit/issues/1554
 */
class UnitOnEmptyBodyConverterFactory : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {

        val nextResponseBodyConverter = retrofit.nextResponseBodyConverter<Any>(this, type, annotations)
        return Converter<ResponseBody, Any> {
            if (it.contentLength() != 0L) {
                nextResponseBodyConverter.convert(it)
            } else Unit
        }
    }
}