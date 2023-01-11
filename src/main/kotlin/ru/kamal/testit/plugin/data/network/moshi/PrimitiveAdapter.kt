package ru.kamal.testit.plugin.data.network.moshi

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonReader

/**
 * Адаптер для безопасной обработки примитивных типов
 * Парсит поля-примитивы пришедшие в DTO от сервера
 * Если поле пришло не в том типе, который ожидается, то вернет null
 * Необходимо добавить адаптер в Moshi, так как по умолчанию, парсинг не того типа полей в Moshi вызывает эксепшен
 */
class PrimitiveAdapter {

    @FromJson
    fun fromJson(jsonReader: JsonReader, delegate: JsonAdapter<Int>): Int? {
        return try {
            delegate.fromJsonValue(jsonReader.nextInt())
        } catch (ex: JsonDataException) {
            jsonReader.skipValue()
            null
        }
    }

    @FromJson
    fun fromJson(jsonReader: JsonReader, delegate: JsonAdapter<String>): String? {
        return when (jsonReader.peek()) {
            JsonReader.Token.BOOLEAN -> jsonReader.nextBoolean().toString()
            else -> try {
                delegate.fromJsonValue(jsonReader.nextString())
            } catch (ex: JsonDataException) {
                jsonReader.skipValue()
                null
            }
        }
    }

    @FromJson
    fun fromJson(jsonReader: JsonReader, delegate: JsonAdapter<Boolean>): Boolean? {
        return when (jsonReader.peek()) {
            JsonReader.Token.STRING -> {
                return when (jsonReader.nextString()) {
                    "true" -> true
                    "false" -> false
                    else -> null
                }
            }
            JsonReader.Token.BOOLEAN -> delegate.fromJsonValue(jsonReader.nextBoolean())
            else -> {
                jsonReader.skipValue()
                null
            }
        }
    }

    @FromJson
    fun fromJson(jsonReader: JsonReader, delegate: JsonAdapter<Double>): Double? {
        return when (jsonReader.peek()) {
            JsonReader.Token.STRING -> {
                val value = jsonReader.nextString().replace(",".toRegex(), ".")
                return try {
                    value.toDouble()
                } catch (ex: NumberFormatException) {
                    null
                }
            }
            JsonReader.Token.NUMBER -> try {
                delegate.fromJsonValue(jsonReader.nextDouble())
            } catch (ex: JsonDataException) {
                jsonReader.skipValue()
                null
            }
            else -> {
                jsonReader.skipValue()
                null
            }
        }
    }

    @FromJson
    fun fromJson(jsonReader: JsonReader, delegate: JsonAdapter<Long>): Long? {
        return try {
            delegate.fromJsonValue(jsonReader.nextLong())
        } catch (ex: JsonDataException) {
            jsonReader.skipValue()
            null
        }
    }
}
