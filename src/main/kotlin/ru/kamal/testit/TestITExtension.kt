package ru.kamal.testit

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property

abstract class TestITExtension {
    abstract val projectId: Property<String>
    abstract val testITUrl: Property<String>
    abstract val configurationId: Property<String>
    abstract val testRunName: Property<String>
    abstract val privateToken: Property<String>
    abstract val namespace: Property<String?>
    abstract val reportDir: DirectoryProperty

    init {
        projectId.finalizeValueOnRead()
        testITUrl.finalizeValueOnRead()
        privateToken.finalizeValueOnRead()
        configurationId.finalizeValueOnRead()
        testRunName.convention("TestRun")
        namespace.convention("namespace")
        reportDir.finalizeValueOnRead()
    }
}