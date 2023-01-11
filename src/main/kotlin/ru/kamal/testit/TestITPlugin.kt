package ru.kamal.testit

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class TestITPlugin : Plugin<Project> {

    companion object {
        private const val EXTENSION_NAME = "testIT"
        private const val GENERATE_TEST_IT_TASK_NAME = "initAutoTestsAndReport"
        private const val DEFAULT_OUTPUT_DIR = "allure-results"
    }

    override fun apply(project: Project): Unit {
        val testITExtension =
            project.extensions.create(EXTENSION_NAME, TestITExtension::class.java)
        testITExtension.reportDir.set(project.layout.buildDirectory.dir(DEFAULT_OUTPUT_DIR))

        val codegenTask: TaskProvider<TestITTask> = project.registerTestITTask(testITExtension)
        project.tasks.withType<KotlinCompile>().configureEach {
            dependsOn(codegenTask)
        }
    }

    private fun Project.registerTestITTask(testITExtension: TestITExtension): TaskProvider<TestITTask> =
        tasks.register<TestITTask>(GENERATE_TEST_IT_TASK_NAME) {
            group = "Test IT"
            description = "Send autotests and results"
            reportDir.set(testITExtension.reportDir)
            projectId.set(testITExtension.projectId)
            configurationId.set(testITExtension.configurationId)
            testRunName.set(testITExtension.testRunName)
            testITUrl.set(testITExtension.testITUrl)
            privateToken.set(testITExtension.privateToken)
            namespace.set(testITExtension.namespace)
        }
}