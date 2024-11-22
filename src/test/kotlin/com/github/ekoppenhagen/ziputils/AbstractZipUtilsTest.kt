package com.github.ekoppenhagen.ziputils

import org.junit.jupiter.api.AfterEach
import java.io.File

abstract class AbstractZipUtilsTest {

    protected val testResources = "src/test/resources"
    protected val tempDirectory = "${testResources}/temp"

    @AfterEach
    fun cleanUp() {
        File(tempDirectory).deleteRecursively()
    }
}
