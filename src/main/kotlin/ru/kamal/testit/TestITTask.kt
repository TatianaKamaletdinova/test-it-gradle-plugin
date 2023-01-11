package ru.kamal.testit

import kotlinx.coroutines.*
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import ru.kamal.testit.plugin.SendManager

abstract class TestITTask : DefaultTask() {

    @get:Input
    abstract val projectId: Property<String>

    @get:Input
    abstract val testITUrl: Property<String>

    @get:Input
    @get:Optional
    abstract val testRunName: Property<String>

    @get:Input
    abstract val configurationId: Property<String>

    @get:Input
    abstract val privateToken: Property<String>

    @get:Input
    @get:Optional
    abstract val namespace: Property<String>

    @get:InputDirectory
    val reportDir: DirectoryProperty = project.objects.directoryProperty()

    @TaskAction
    fun taskAction() {
        println("reportDir: ${reportDir.get().asFile}")

        CoroutineScope(Dispatchers.Main + SupervisorJob()).launch {
            SendManager(
                testITUrl.get(),
                projectId.get(),
                namespace.get(),
                configurationId.get(),
                reportDir.get().asFile,
                testRunName.get()
            )
                .initWorkTestIT()
        }
    }
}