package ru.kamal.testit.plugin.util.parser

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class JsonHelper(
    private val defaultMoshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
) {

    fun <T> fromJson(json: String, objClass: Class<T>): T? {
        if (json.isEmpty()) return null
        return try {
            jsonAdapter(objClass).fromJson(json)
        } catch (e: Exception) {
            null
        }
    }

    fun <T> toJson(obj: T, objClass: Class<T>): String =
        jsonAdapter(objClass).toJson(obj)

    fun <T> fromJsonList(json: String, objClass: Class<T>): List<T>? {
        if (json.isEmpty()) return null
        return try {
            jsonListAdapter(objClass).fromJson(json)
        } catch (e: Exception) {
            null
        }
    }

    fun <T> toJsonList(objList: List<T>, objClass: Class<T>): String =
        jsonListAdapter(objClass).toJson(objList)

    private fun <T> jsonAdapter(objClass: Class<T>): JsonAdapter<T> =
        defaultMoshi.adapter(objClass)

    private fun <T> jsonListAdapter(objClass: Class<T>): JsonAdapter<List<T>> =
        defaultMoshi.adapter(Types.newParameterizedType(List::class.java, objClass))
}