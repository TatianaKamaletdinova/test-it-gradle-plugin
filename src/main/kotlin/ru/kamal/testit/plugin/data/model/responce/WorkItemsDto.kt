package ru.kamal.testit.plugin.data.model.responce

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WorkItemsDto(
    val autoTests: List<AutoTest>,
    val createdById: String?,
    val createdDate: String?,
    val description: String?,
    val duration: Int?,
    val entityTypeName: String?,
    val globalId: Int?,
    val id: String?,
    val isAutomated: Boolean?,
    val isDeleted: Boolean?,
    val iterations: List<Any?>?,
    val links: List<Any?>?,
    val medianDuration: Int?,
    val modifiedById: String?,
    val modifiedDate: String?,
    val name: String?,
    val postconditionSteps: List<Any?>?,
    val preconditionSteps: List<PreconditionStep?>?,
    val priority: String?,
    val projectId: String?,
    val sectionId: String?,
    val sectionPostconditionSteps: List<Any?>?,
    val sectionPreconditionSteps: List<Any?>?,
    val state: String?,
    val steps: List<Steps?>?,
    val tags: List<Any?>?,
    val versionId: String?,
    val versionNumber: Int?
)

data class AutoTest(
    val classname: Any?,
    val createdById: String?,
    val createdDate: String?,
    val description: Any?,
    val externalId: String?,
    val globalId: Int?,
    val id: String?,
    val isDeleted: Boolean?,
    val isFlaky: Boolean?,
    val labels: List<Any?>?,
    val lastTestResultId: Any?,
    val lastTestResultOutcome: Any?,
    val lastTestRunId: Any?,
    val lastTestRunName: Any?,
    val links: List<Any?>?,
    val modifiedById: String?,
    val modifiedDate: String?,
    val mustBeApproved: Any?,
    val name: String?,
    val namespace: String?,
    val projectId: String?,
    val setup: List<Any?>?,
    val stabilityPercentage: Int?,
    val steps: List<Any?>?,
    val teardown: List<Any?>?,
    val title: Any?
)

data class PreconditionStep(
    val action: String?,
    val comments: String?,
    val expected: String?,
    val id: String?,
    val testData: String?,
    val workItem: WorkItem?,
    val workItemId: String?
)

data class Steps(
    val action: String?,
    val comments: String?,
    val expected: String?,
    val id: String?,
    val testData: String?,
    val workItem: Any?,
    val workItemId: Any?
)

data class WorkItem(
    val globalId: Int?,
    val isDeleted: Boolean?,
    val name: String?,
    val steps: List<Steps?>?,
    val versionId: String?
)