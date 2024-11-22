package com.github.ekoppenhagen.ziputils.exceptions

/**
 * Thrown to indicate that a comparison of two zipped objects failed.
 *
 * @param[comparisonError] the detail message why the comparison failed.
 *
 * @since 0.1.0
 * @author Erik Koppenhagen
 */
class ZipComparisonException(
    comparisonError: String,
) : Exception("failed to compare zip files: $comparisonError")
