package ru.kamal.testit.plugin.data.network.moshi

import com.squareup.moshi.Json
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * Использует значение из аннотации [Json] вместо названия поля Enum при конвертации
 * значения в Query, Field, Path параметра Retrofit. Если аннотации нет то используется метод [toString]
 */
class EnumConverterFactory : Converter.Factory() {

    override fun stringConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<*, String>? = if (type is Class<*> && type.isEnum) {
        Converter<Any?, String> { value ->
            getSerializedNameValue(value as Enum<*>) ?: value.toString()
        }
    } else {
        null
    }

    private fun <E : Enum<*>> getSerializedNameValue(e: E): String? = try {
        e.javaClass.getField(e.name).getAnnotation(Json::class.java)?.name
    } catch (exception: NoSuchFieldException) {
        exception.printStackTrace()
        null
    }
}