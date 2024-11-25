package io.github.ekoppenhagen.ziputils.exceptions

import java.io.File

/**
 * Thrown to indicate that a file already exists and won't be overwritten.
 *
 * @param[existingFile] the existing file.
 *
 * @since 0.2.0
 * @author Erik Koppenhagen
 */
class TargetAlreadyExistsException(
    existingFile: File,
) : Exception("failed to create new zip file: ${existingFile.absolutePath} already exists.")
