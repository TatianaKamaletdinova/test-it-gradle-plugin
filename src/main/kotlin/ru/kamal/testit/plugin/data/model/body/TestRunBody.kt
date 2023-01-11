package ru.kamal.testit.plugin.data.model.body

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TestRunBody(
    val projectId: String,
    val name: String,
)