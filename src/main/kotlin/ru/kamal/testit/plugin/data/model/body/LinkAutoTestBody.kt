package ru.kamal.testit.plugin.data.model.body

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LinkAutoTestBody(val id: String)