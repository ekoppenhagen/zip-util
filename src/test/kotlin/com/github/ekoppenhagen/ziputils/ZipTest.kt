package com.github.ekoppenhagen.ziputils

import com.github.ekoppenhagen.ziputils.exceptions.TargetAlreadyExistsException
import com.github.ekoppenhagen.ziputils.exceptions.ZipComparisonException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldEndWith
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.io.File

class ZipTest : AbstractZipUtilsTest() {

    @AfterEach
    fun deleteEmptyFolder() {
        File("$testResources/Test_Directory/emptyDirectory").deleteRecursively()
    }

    @Nested
    inner class ZipFile {

        @Test
        fun `zip directory with default target`() {
            File("$tempDirectory/").mkdir()
            val sourceFile = File("$tempDirectory/Test_Directory")
            File("$testResources/Test_Directory").copyRecursively(sourceFile)
            // create additional empty directory which can't be committed
            File("$tempDirectory/Test_Directory/emptyDirectory").mkdir()
            val expectedOutcome = File("$testResources/Test_Directory.zip")

            val result = Zip.file(sourceFile)

            CompareZip.files(expectedOutcome, result)
        }

        @Test
        fun `zip directory with default target which already exists`() {
            val sourceFile = File("$testResources/Test_Directory")

            val expectedException = shouldThrow<TargetAlreadyExistsException> {
                Zip.file(sourceFile)
            }
            expectedException.message shouldEndWith "${sourceFile.absolutePath}.zip already exists."
        }

        @Test
        fun `zip directory with default target which already exists and overwrite it`() {
            File("$tempDirectory/").mkdir()
            val sourceFile = File("$tempDirectory/Test_Directory")
            File("$testResources/Test_Directory").copyRecursively(sourceFile)
            // create additional empty directory which can't be committed
            File("$tempDirectory/Test_Directory/emptyDirectory").mkdir()
            File("$testResources/Test_Directory_unequal.zip").copyRecursively(File("$tempDirectory/Test_Directory.zip"))
            val expectedOutcome = File("$testResources/Test_Directory.zip")

            val result = Zip.file(sourceFile, overwriteExistingFile = true)

            CompareZip.files(expectedOutcome, result)
        }

        @Test
        fun `zip directory to specific target file`() {
            // create additional empty directory which can't be committed
            File("$testResources/Test_Directory/emptyDirectory").mkdir()
            val sourceFile = File("$testResources/Test_Directory")
            File("$tempDirectory/").mkdir()
            val targetFile = File("$tempDirectory/Test_Directory.zip")
            val expectedOutcome = File("$testResources/Test_Directory.zip")

            val result = Zip.file(sourceFile, target = targetFile)

            result.absolutePath shouldBe targetFile.absolutePath
            CompareZip.files(expectedOutcome, result)
        }

        @Test
        fun `zip directory to specific target file which already exists`() {
            File("$tempDirectory/").mkdir()
            val sourceFile = File("$tempDirectory/Test_Directory")
            File("$testResources/Test_Directory").copyRecursively(sourceFile)
            // create additional empty directory which can't be committed
            File("$tempDirectory/Test_Directory/emptyDirectory").mkdir()
            val targetFile = File("$testResources/Test_Directory.zip")

            val expectedException = shouldThrow<TargetAlreadyExistsException> {
                Zip.file(sourceFile, target = targetFile)
            }
            expectedException.message shouldEndWith "${targetFile.absolutePath} already exists."
        }

        @Test
        fun `zip directory to specific target which already exists and overwrite it`() {
            File("$tempDirectory/").mkdir()
            val sourceFile = File("$tempDirectory/Test_Directory")
            File("$testResources/Test_Directory").copyRecursively(sourceFile)
            // create additional empty directory which can't be committed
            File("$tempDirectory/Test_Directory/emptyDirectory").mkdir()
            val targetFile = File("$tempDirectory/Test_Directory.zip")
            File("$testResources/Test_Directory_unequal.zip").copyRecursively(targetFile)
            val expectedOutcome = File("$testResources/Test_Directory.zip")

            val result = Zip.file(sourceFile, target = targetFile, overwriteExistingFile = true)

            CompareZip.files(expectedOutcome, result)
        }
    }

    @Nested
    inner class ZipByteArray {

        @Test
        fun `zip byte array`() {
            val sourceArray = File("$testResources/Test_Directory/text.txt").readBytes()
            val expectedOutcome = File("$testResources/text.zip").readBytes()

            val result = Zip.byteArray(sourceArray, contentName = "text.txt")

            CompareZip.byteArrays(expectedOutcome, result)
        }

        @Test
        fun `zip byte array and change file name of content`() {
            val sourceArray = File("$testResources/Test_Directory/text.txt").readBytes()
            val expectedOutcome = File("$testResources/text.zip").readBytes()

            val result = Zip.byteArray(sourceArray, contentName = "changed_text")

            val expectedException = shouldThrow<ZipComparisonException> {
                CompareZip.byteArrays(expectedOutcome, result)
            }
            expectedException.message shouldEndWith "zip files contain different files/directories: text.txt <-> changed_text"
        }
    }
}
