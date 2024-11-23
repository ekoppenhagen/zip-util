package com.github.ekoppenhagen.ziputils

import com.github.ekoppenhagen.ziputils.exceptions.TargetAlreadyExistsException
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/**
 * A collection of functions to zip objects.
 *
 * @since 0.2.0
 * @author Erik Koppenhagen
 */
object Zip {

    /**
     * Compresses a file. Works with directories and files.
     *
     * @param[source] the [File][java.io.File] to compress.
     * @param[target] the resulting compressed [File][java.io.File] (default: create a new file at the same location called `<source_file_name>.zip`).
     * @param[overwriteExistingFile] don't fail if the target already exists and overwrite it (default: `false`).
     *
     * @return the resulting compressed file.
     *
     * @exception TargetAlreadyExistsException
     * if the target file already exists and
     * `overwriteExistingFile` is set to `false`.
     *
     * @since 0.2.0
     * @author Erik Koppenhagen
     */
    @Throws(TargetAlreadyExistsException::class)
    fun file(
        source: File,
        target: File = File("${source.absoluteFile}.zip"),
        overwriteExistingFile: Boolean = false,
    ): File {
        checkForAlreadyExistingTarget(target, overwriteExistingFile)
        ByteArrayOutputStream().use {
            ZipOutputStream(it).use { zipOutputStream ->
                zip(source, source.name, zipOutputStream)
            }
            target.writeBytes(it.toByteArray())
            return target
        }
    }

    @Throws(TargetAlreadyExistsException::class)
    private fun checkForAlreadyExistingTarget(target: File, overwriteExistingFile: Boolean) {
        if (!overwriteExistingFile && target.exists()) throw TargetAlreadyExistsException(target)
    }

    /**
     * Compresses a byte array. Does not work for directories.
     *
     * @param[source] the [ByteArray][kotlin.ByteArray] to compress.
     * @param[contentName] the file name of the content, including the file ending. _This is not the name of the resulting zip_.
     *
     * @return the resulting compressed byte array.
     *
     * @since 0.2.0
     * @author Erik Koppenhagen
     */
    fun byteArray(
        source: ByteArray,
        contentName: String,
    ): ByteArray {

        ByteArrayOutputStream().use {
            ZipOutputStream(it).use { zipOutputStream ->
                zipByteArray(contentName, source, zipOutputStream)
            }
            return it.toByteArray()
        }
    }

    private fun zipByteArray(
        targetName: String,
        source: ByteArray,
        zipOutputStream: ZipOutputStream,
    ) {
        val zippedContent = ZipEntry(targetName)
        zippedContent.size = source.size.toLong()

        zipOutputStream.putNextEntry(zippedContent)
        zipOutputStream.write(source)
        zipOutputStream.closeEntry()
    }

    @Suppress("MemberNameEqualsClassName")
    private fun zip(
        source: File,
        sourceName: String,
        zipOutputStream: ZipOutputStream,
    ) {
        if (source.isDirectory) zipDirectory(source, sourceName, zipOutputStream)
        else zipFile(source, sourceName, zipOutputStream)
    }

    private fun zipDirectory(
        sourceDirectory: File,
        directoryName: String,
        zipOutputStream: ZipOutputStream
    ) {
        val adjustedDirectoryName = addSuffixIfNotExists(directoryName, "/")
        zipOutputStream.putNextEntry(ZipEntry(adjustedDirectoryName))
        zipOutputStream.closeEntry()

        sourceDirectory.listFiles()?.forEach {
            zip(it, "$adjustedDirectoryName${it.name}", zipOutputStream)
        }
    }

    private fun addSuffixIfNotExists(text: String, suffix: String) =
        if (text.endsWith(suffix)) text
        else "$text$suffix"

    private fun zipFile(
        sourceFile: File,
        fileName: String,
        zipOutputStream: ZipOutputStream,
    ) {
        zipOutputStream.putNextEntry(ZipEntry(fileName))
        zipOutputStream.write(sourceFile.readBytes())
        zipOutputStream.closeEntry()
    }
}
