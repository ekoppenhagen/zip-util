plugins {
    // https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-bom
    kotlin("jvm") version "1.9.23"
    // https://mvnrepository.com/artifact/io.gitlab.arturbosch.detekt/detekt-gradle-plugin
    id("io.gitlab.arturbosch.detekt") version "1.23.6"
}

group = "com.github.ekoppenhagen"
version = "0.1.0"

repositories(RepositoryHandler::mavenCentral)
kotlin { jvmToolchain(21) }
detekt {
    // https://mvnrepository.com/artifact/io.gitlab.arturbosch.detekt/detekt-gradle-plugin
    toolVersion = "1.23.6"
    config.from(file("config/detekt.yml"))
    buildUponDefaultConfig = true
    allRules = false
}
tasks.test {
    useJUnitPlatform()
}
testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            // https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter
            useJUnitJupiter("5.10.2")
        }
    }
}
dependencies {
    // https://mvnrepository.com/artifact/io.gitlab.arturbosch.detekt/detekt-gradle-plugin
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.6")
    // https://mvnrepository.com/artifact/io.kotest/kotest-runner-junit5
    testImplementation("io.kotest:kotest-runner-junit5:5.8.1")
}
