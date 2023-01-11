plugins {
    `kotlin-dsl`
    `maven-publish`
    kotlin("jvm") version "1.8.0"
    id("org.jetbrains.kotlin.kapt") version "1.8.0"
}

gradlePlugin {
    plugins {
        register("test-it-gradle-plugin") {
            group = "ru.kamal.testit"
            id = "ru.kamal.testit"
            version = "0.0.1"
            implementationClass = "ru.kamal.testit.TestITPlugin"
            description = "Plugin for send autotests and them results to Test IT"
            displayName = "TestITPlugin"
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    //json
    implementation("com.squareup.moshi:moshi-kotlin:1.14.0")
    implementation("com.squareup.moshi:moshi:1.14.0")
    implementation("com.squareup.moshi:moshi-adapters:1.14.0")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.14.0")

    //network
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")

    //thread
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")

    //kotlin-gradle-plugin
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.0")

    testImplementation("junit:junit:4.12")
    testImplementation("io.mockk:mockk:1.11.0")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.10.0")
}