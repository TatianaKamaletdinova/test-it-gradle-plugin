package ru.kamal.testit.plugin.data.model

import com.squareup.moshi.JsonClass
import ru.kamal.testit.plugin.data.model.body.Link
import ru.kamal.testit.plugin.data.model.body.TestResultsBody
import java.text.SimpleDateFormat
import java.util.*

data class AllureReport(
    val attachments: List<Attachment?>?,
    val description: Any?,
    val descriptionHtml: Any?,
    val fullName: String?,
    val historyId: String?,
    val labels: List<Label?>?,
    val links: List<AllureLink?>?,
    val name: String?,
    val parameters: List<Any?>?,
    val stage: String?,
    val start: Long?,
    val status: String?,
    val statusDetails: Any?,
    val steps: List<Any>?,
    val stop: Long?,
    val uuid: String?,
    val attachmentIds: List<AttachmentId?>?
)

data class Attachment(
    val name: String?,
    val source: String?,
    val type: String?
)

data class Label(
    val name: String?,
    val value: String?
)

data class AllureLink(
    val name: String?,
    val type: String?,
    val url: String?
)

@JsonClass(generateAdapter = true)
data class AttachmentId(
    val id: String
)

fun AllureReport.mapToTestResultsBody(configurationId: String): TestResultsBody {
    val startTime = convertLongToTime(start ?: 0)
    val stopTime = convertLongToTime(stop ?: 0)
    return TestResultsBody(
        attachments = this.attachmentIds?.mapNotNull { it } ?: emptyList(),
        autoTestExternalId = historyId ?: throw NullPointerException("historyId is null"),

        startedOn = startTime.toTimeFormat(),
        completedOn = stopTime.toTimeFormat(),
        duration = (stopTime.time - startTime.time).toInt(),

        configurationId = configurationId,

        links = links?.mapNotNull {
            Link(
                it?.name ?: return@mapNotNull null,
                it.type ?: return@mapNotNull null,
                it.url ?: return@mapNotNull null,
            )
        } ?: emptyList(),

        outcome = status?.capitalize() ?: throw NullPointerException("status is null"),
    )
}

fun convertLongToTime(time: Long): Date = Date(time)

fun Date.toTimeFormat(): String {
    val format = SimpleDateFormat("yyyy.MM.dd HH:mm")
    return format.format(date)
}