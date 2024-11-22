package com.github.ekoppenhagen.ziputils

import com.github.ekoppenhagen.ziputils.CompareZip.byteArrays
import com.github.ekoppenhagen.ziputils.CompareZip.files
import com.github.ekoppenhagen.ziputils.exceptions.ZipComparisonException
import java.io.File
import java.io.InputStream
import java.util.zip.ZipInputStream

/**
 * A collection of functions to compare zipped objects content-wise.
 *
 * @since 0.1.0
 * @author Erik Koppenhagen
 */
object CompareZip {

    /**
     * Compares two zipped files and throws an exception if they are not equal.
     *
     * @param[zippedFile1] the first [File][java.io.File] to compare.
     * @param[zippedFile2] the second [File][java.io.File] to compare.
     * @param[enableFileNameComparison] enable comparison by file name before comparison by content (default: `true`).
     *
     * @exception ZipComparisonException
     * if either the files have different names,
     * the zipped directories have different content,
     * or the content of two files is not equal.
     *
     * @since 0.1.0
     * @see byteArrays
     * @author Erik Koppenhagen
     */
    @Throws(ZipComparisonException::class)
    fun files(
        zippedFile1: File,
        zippedFile2: File,
        enableFileNameComparison: Boolean = true,
    ) {
        if (enableFileNameComparison) compareFileNames(zippedFile1, zippedFile2)
        byteArrays(zippedFile1.readBytes(), zippedFile2.readBytes())
    }

    /**
     * Compares two byte arrays of zipped content and throws an exception if they are not equal.
     *
     * @param[zippedArray1] the first [ByteArray][java.io.File] to compare.
     * @param[zippedArray2] the second [ByteArray][java.io.File] to compare.
     *
     * @exception ZipComparisonException
     * if the zipped directories have different content,
     * or the content of two files is not equal.
     *
     * @since 0.1.0
     * @see files
     * @author Erik Koppenhagen
     */
    @Throws(ZipComparisonException::class)
    fun byteArrays(
        zippedArray1: ByteArray,
        zippedArray2: ByteArray,
    ) {
        val zippedArray1Content = getZipContent(zippedArray1.inputStream())
        val zippedArray2Content = getZipContent(zippedArray2.inputStream())
        compareContent(zippedArray1Content, zippedArray2Content)
    }

    @Throws(ZipComparisonException::class)
    private fun compareFileNames(zipFile1: File, zipFile2: File) {
        if (zipFile1.name != zipFile2.name)
            throw ZipComparisonException("zip files have different names")
    }

    private fun getZipContent(inputStream: InputStream) =
        ZipInputStream(inputStream).use {
            generateSequence { it.nextEntry }
                .map { zipEntry -> zipEntry.name to it.readAllBytes() }
                .toList()
                .sortedBy(Pair<String, ByteArray>::first)
        }

    @Throws(ZipComparisonException::class)
    private fun compareContent(
        zipFile1Content: List<Pair<String, ByteArray>>,
        zipFile2Content: List<Pair<String, ByteArray>>,
    ) {
        zipFile1Content
            .zip(zipFile2Content)
            .forEach { (zipFile1Element, zipFile2Element) ->
                if (zipFile1Element.first != zipFile2Element.first)
                    throw ZipComparisonException("zip files contain different files/directories")
                if (!zipFile1Element.second.contentEquals(zipFile2Element.second))
                    throw ZipComparisonException("contents of file '${zipFile1Element.first}' are not equal")
            }
    }
}
