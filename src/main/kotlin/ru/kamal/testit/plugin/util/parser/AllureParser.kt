package ru.kamal.testit.plugin.util.parser

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.kamal.testit.plugin.data.model.AllureReport

class AllureParser {
    private val jsonHelper = JsonHelper()

    suspend fun parse(json: String): AllureReport? = withContext(Dispatchers.IO) {
        jsonHelper.fromJson(json, AllureReport::class.java)
    }
}