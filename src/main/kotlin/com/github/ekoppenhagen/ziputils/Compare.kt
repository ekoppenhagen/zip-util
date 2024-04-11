package com.github.ekoppenhagen.ziputils

import java.io.File
import java.io.InputStream
import java.util.zip.ZipInputStream

object Compare {

    fun files(
        zippedFile1: File,
        zippedFile2: File,
        skipFileNameComparison: Boolean = false,
    ) {
        if (!skipFileNameComparison) compareFileNames(zippedFile1, zippedFile2)
        byteArrays(zippedFile1.readBytes(), zippedFile2.readBytes())
    }

    fun byteArrays(
        zippedArray1: ByteArray,
        zippedArray2: ByteArray,
    ) {
        val zippedArray1Content = getZipContent(zippedArray1.inputStream())
        val zippedArray2Content = getZipContent(zippedArray2.inputStream())
        compareContent(zippedArray1Content, zippedArray2Content)
    }

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
