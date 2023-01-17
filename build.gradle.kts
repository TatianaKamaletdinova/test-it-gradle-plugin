plugins {
    `kotlin-dsl`
    `maven-publish`
    `java-gradle-plugin`
    signing
    kotlin("jvm") version "1.8.0"
    id("org.jetbrains.kotlin.kapt") version "1.8.0"
    id("com.gradle.plugin-publish") version "1.0.0"
}

group = "io.github.tatianakamaletdinova.testit-plugin-gradle"
version = "0.0.3"

// Configure java-gradle-plugin
gradlePlugin {
    uri("https://github.com/tatianakamaletdinova/test-it-gradle-plugin")

    plugins {
        register("testITGradlePlugin") {
            id = "io.github.tatianakamaletdinova.testit-plugin-gradle"
            implementationClass = "ru.kamal.testit.TestITPlugin"
            description = "Plugin for send autotests and them results to Test IT"
            displayName = "TestITPlugin"
            uri("https://github.com/tatianakamaletdinova/test-it-gradle-plugin")
        }
    }
}

// Configure plugin-publish plugin
pluginBundle {
    description = "Plugin for send autotests and them results to Test IT"
    website = "https://github.com/tatianakamaletdinova/test-it-gradle-plugin"
    vcsUrl = "https://github.com/tatianakamaletdinova/test-it-gradle-plugin"
    tags = mutableListOf("Test IT", "testing", "test report")
}

publishing {
    publications {
        create<MavenPublication>("testITGradlePlugin") {
            artifactId = "test-it-gradle-plugin"
            groupId = "io.github.tatianakamaletdinova.testit-plugin-gradle"
            version = "0.0.3"
            from(components["kotlin"])

            pom {
                name.set(project.name)
                description.set(project.description)
                url.set("https://github.com/tatianakamaletdinova/test-it-gradle-plugin")

                scm {
                    url.set("https://github.com/tatianakamaletdinova/test-it-gradle-plugin")
                }

                licenses {
                    license {
                        name.set("The Apache Software License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                        distribution.set("repo")
                    }
                }

                developers {
                    developer {
                        id.set("ITSurgeon")
                        name.set("Tatiana Kamaletdinova")
                        email.set("kamaltatiana@yandex.ru")
                    }
                }
            }
        }
    }
}

signing {
    sign(publishing.publications["testITGradlePlugin"])
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
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

    //kotlin-gradle-plugin
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.0")

    testImplementation("junit:junit:4.12")
    testImplementation("io.mockk:mockk:1.11.0")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.10.0")
}