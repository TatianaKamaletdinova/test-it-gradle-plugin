package ru.kamal.testit.plugin.data.model.responce

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseProjectDto(
    val attributesScheme: List<AttributesScheme?>?,
    val autoTestsCount: Int?,
    val checkListsCount: Int?,
    val createdById: String?,
    val createdDate: String?,
    val description: String?,
    val globalId: Int?,
    val id: String?,
    val isDeleted: Boolean?,
    val isFavorite: Boolean?,
    val modifiedById: String?,
    val modifiedDate: String?,
    val name: String?,
    val sharedStepsCount: Int?,
    val testCasesCount: Int?,
    val testPlansAttributesScheme: List<TestPlansAttributesScheme?>?
)

@JsonClass(generateAdapter = true)
data class AttributesScheme(
    val enabled: Boolean?,
    val id: String?,
    val isDeleted: Boolean?,
    val isGlobal: Boolean?,
    val name: String?,
    val options: List<Option>?,
    val required: Boolean?,
    val type: String?
)

@JsonClass(generateAdapter = true)
data class TestPlansAttributesScheme(
    val enabled: Boolean?,
    val id: String?,
    val isDeleted: Boolean?,
    val isGlobal: Boolean?,
    val name: String?,
    val options: List<Option?>?,
    val required: Boolean?,
    val type: String?
)

@JsonClass(generateAdapter = true)
data class Option(
    val id: String?,
    val isDefault: Boolean?,
    val isDeleted: Boolean?,
    val value: String?
)