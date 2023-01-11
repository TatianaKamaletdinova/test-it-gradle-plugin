package ru.kamal.testit.plugin.data.model.body

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateAutoTestBody(
    val shouldCreateWorkItem: Boolean,
    val externalId: String,
    val links: List<LinksAutoTest>,
    val labels: List<LabelsAutoTest>,
    val projectId: String,
    val name: String,
    val namespace: String,
    val isFlaky: Boolean,
) {

    @JsonClass(generateAdapter = true)
    data class LinksAutoTest(
        val title: String,
        val url: String,
        val type: String,
        val hasInfo: Boolean,
    )

    @JsonClass(generateAdapter = true)
    data class LabelsAutoTest(
        val name: String,
        val value: String,
    )
}