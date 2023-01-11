package ru.kamal.testit.plugin.data.model.responce

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class ResponseCreatedAutoTestDto(
    val globalId: Int? = null
)