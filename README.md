# 🗜 zip-utils
![Version](https://img.shields.io/badge/version-0.2.1-red.svg?cacheSeconds=2592000)
[![GitHub last commit](https://img.shields.io/github/last-commit/ekoppenhagen/zip-utils)](#)
[![License](https://img.shields.io/badge/License-MIT%20License-green.svg)](https://github.com/ekoppenhagen/zip-utils/blob/main/LICENSE)

[![Supported_Language](https://img.shields.io/badge/Kotlin-%237F52FF.svg?logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Supported_Language](https://img.shields.io/badge/Java-%23ED8B00.svg?logo=openjdk&logoColor=white)](https://www.java.com/de/)

⭐ Star me on GitHub — it motivates me a lot!

## 📖 Table of Contents

- [About](#-about)
- [Installation](#-installation)
- [Examples](#-usage)
  - [zip](#-zip)
  - [compare](#-compare)
  - [unzip](#-unzip)
- [License](#-license)
- [Contact](#-contact)

## 💡 About

Open source library for working with zip files with JVM-based languages.
Uses native functions only and does not bring any transitive third-party dependencies.
Supports zipping, unzipping and comparing of zipped files.

## ⚙️ Installation

Just add this project to your dependencies and you are good to go.

### Gradle

```kotlin
implementation("com.github.ekoppenhagen:zip-utils:0.2.1")
```

### Maven

```xml
<dependency>
    <groupId>com.github.ekoppenhagen</groupId>
    <artifactId>zip-utils</artifactId>
    <version>0.2.1</version>
</dependency>
```

## 👀 Examples

Listed below are _some_ of the common usages.

### ➡ zip

```kotlin
private fun storeConfiguration(config: File) =
    Zip.file(myFile, overwriteExistingFile = true)
```

```kotlin
private fun compressIncomingFile(incomingFile: ByteArray, fileName: String) =
    Zip.byteArray(incomingFile, fileName)
```

### ⬌ compare

```kotlin
private fun validateZip(zipFile: File) =
    runCatching { CompareZip.files(zipFile, referenceFile) }
        .onFailure { logger.error("{}", it.localizedMessage) }
        .isSuccess
```

```kotlin

@Test
fun `incoming zip should not equal exiting file`() {
    ...
    val expectedException = shouldThrow<ZipComparisonException> {
        CompareZip.byteArrays(incomingRequest, existingFile)
    }
    expectedException.message shouldEndWith "zip files contain different files/directories: reference/setup.sh <-> reference/update.sh"
}
```

### ⬅ unzip

_TBD_

## 📃 License
This library is published under the [MIT license](https://mit-license.org/),
making it free to use for commercial and non-commercial purposes.

## 🗨️ Contact

Feel free to take a look at my other work or contact me:

[![GitHub](https://img.shields.io/badge/GitHub-%23121011.svg?logo=github&logoColor=white)](https://github.com/ekoppenhagen)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-0A66C2?logo=linkedin&logoColor=fff)](https://www.linkedin.com/in/erik-koppenhagen/)
