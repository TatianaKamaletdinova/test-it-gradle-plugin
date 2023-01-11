package ru.kamal.testit.plugin.data.model.responce

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetAutoTestDtoItem(
    val classname: Any?,
    val createdById: String?,
    val createdDate: String?,
    val description: Any?,
    val externalId: String?,
    val globalId: Int?,
    val id: String?,
    val isDeleted: Boolean?,
    val isFlaky: Boolean?,
    val labels: List<Label?>?,
    val lastTestResultId: Any?,
    val lastTestResultOutcome: Any?,
    val lastTestRunId: Any?,
    val lastTestRunName: Any?,
    val links: List<Link?>?,
    val modifiedById: String?,
    val modifiedDate: String?,
    val mustBeApproved: Boolean?,
    val name: String?,
    val namespace: String?,
    val projectId: String?,
    val setup: List<Any?>?,
    val stabilityPercentage: Any?,
    val steps: List<Any?>?,
    val teardown: List<Any?>?,
    val title: Any?
)

@JsonClass(generateAdapter = true)
data class Label(
    val globalId: Int?,
    val name: String?
)

@JsonClass(generateAdapter = true)
data class Link(
    val description: Any?,
    val hasInfo: Boolean?,
    val id: String?,
    val title: Any?,
    val type: String?,
    val url: String?
)