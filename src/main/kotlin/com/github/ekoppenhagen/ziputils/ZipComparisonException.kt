package com.github.ekoppenhagen.ziputils

class ZipComparisonException(
    comparisonError: String,
) : Exception("failed to compare zip files: $comparisonError")
