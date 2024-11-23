package com.github.ekoppenhagen.ziputils

import com.github.ekoppenhagen.ziputils.exceptions.ZipComparisonException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.string.shouldEndWith
import org.junit.jupiter.api.Test
import java.io.File

class CompareTest : AbstractZipUtilsTest() {

    @Test
    fun `compare two identical zip files with the same name`() {
        val referenceZipFile = File("$testResources/Test_Directory.zip")
        val zipFileCopy = File("$tempDirectory/Test_Directory.zip")
        referenceZipFile.copyTo(zipFileCopy)

        CompareZip.files(referenceZipFile, zipFileCopy)
    }

    @Test
    fun `compare two identical zip files with the same name while skipping file name comparison`() {
        val referenceZipFile = File("$testResources/Test_Directory.zip")
        val zipFileCopy = File("$tempDirectory/Test_Directory.zip")
        referenceZipFile.copyTo(zipFileCopy)

        CompareZip.files(referenceZipFile, zipFileCopy, enableFileNameComparison = false)
    }

    @Test
    fun `compare two identical zip files with different names`() {
        val referenceZipFile = File("$testResources/Test_Directory.zip")
        val zipFileCopy = File("$tempDirectory/Test_Directory_Copy.zip")
        referenceZipFile.copyTo(zipFileCopy)

        val expectedException = shouldThrow<ZipComparisonException> {
            CompareZip.files(referenceZipFile, zipFileCopy)
        }
        expectedException.message shouldEndWith "zip files have different names"
    }

    @Test
    fun `compare two identical zip files with different names while skipping file name comparison`() {
        val referenceZipFile = File("$testResources/Test_Directory.zip")
        val zipFileCopy = File("$tempDirectory/Test_Directory_Copy.zip")
        referenceZipFile.copyTo(zipFileCopy)

        CompareZip.files(referenceZipFile, zipFileCopy, enableFileNameComparison = false)
    }

    @Test
    fun `compare two zip files with different content`() {
        val referenceZipFile = File("$testResources/Test_Directory.zip")
        val otherDirectory = File("$testResources/Test_Directory")
        File("$tempDirectory/").mkdir()
        val otherZipFile = Zip.file(otherDirectory, File("$tempDirectory/Test_Directory.zip"))

        val expectedException = shouldThrow<ZipComparisonException> {
            CompareZip.files(referenceZipFile, otherZipFile)
        }
        expectedException.message shouldEndWith "zip files contain different files/directories: Test_Directory/emptyDirectory/ <-> Test_Directory/sample.zip"
    }

    @Test
    fun `compare two zip files with different file content`() {
        val referenceZipFile = File("$testResources/Test_Directory.zip")
        val otherZipFile = File("$testResources/Test_Directory_unequal.zip")

        val expectedException = shouldThrow<ZipComparisonException> {
            CompareZip.files(referenceZipFile, otherZipFile, enableFileNameComparison = false)
        }
        expectedException.message shouldEndWith "contents of file 'Test_Directory/subDirectory/picture.png' are not equal"
    }

    @Test
    fun `compare two zip byte arrays with different file content`() {
        val referenceZipByteArray = File("$testResources/Test_Directory.zip").readBytes()
        val otherZipByteArray = File("$testResources/Test_Directory_unequal.zip").readBytes()

        val expectedException = shouldThrow<ZipComparisonException> {
            CompareZip.byteArrays(referenceZipByteArray, otherZipByteArray)
        }
        expectedException.message shouldEndWith "contents of file 'Test_Directory/subDirectory/picture.png' are not equal"
    }
}
