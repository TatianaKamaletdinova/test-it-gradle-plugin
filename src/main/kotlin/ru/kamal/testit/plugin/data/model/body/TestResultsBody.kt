package ru.kamal.testit.plugin.data.model.body

import com.squareup.moshi.JsonClass
import ru.kamal.testit.plugin.data.model.AttachmentId

@JsonClass(generateAdapter = true)
data class TestResultsBody(
    val attachments: List<AttachmentId>,
    val autoTestExternalId: String,

    val startedOn: String,
    val completedOn: String,
    val duration: Int,

    val configurationId: String,

    val failureReasonName: Any? = null,

    val links: List<Link>,

    val message: Any? = null,

    val outcome: String,

    val parameters: Parameters? = null,
    val setupResults: List<Any> = emptyList(),
    val stepResults: List<Any> = emptyList(),
    val teardownResults: List<Any> = emptyList()
)

@JsonClass(generateAdapter = true)
data class Link(
    val name: String,
    val type: String,
    val url: String
)

@JsonClass(generateAdapter = true)
class Parameters