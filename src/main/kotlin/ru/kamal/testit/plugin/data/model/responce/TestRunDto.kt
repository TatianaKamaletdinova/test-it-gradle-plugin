package ru.kamal.testit.plugin.data.model.responce

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TestRunDto(val id: String?)