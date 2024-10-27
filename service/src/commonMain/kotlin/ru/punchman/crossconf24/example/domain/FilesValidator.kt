package ru.punchman.crossconf24.example.domain

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure

class FilesValidator {

    fun validate(files: List<String>): Either<ArtifactsError, Unit> = either {
        ensure(files.isNotEmpty()) {
            ArtifactsError.EMPTY
        }
        val invalidFiles = files.filter { !it.endsWith(".apk") }
        ensure(invalidFiles.isEmpty()) {
            ArtifactsError.INVALID_TYPE
        }
    }
}

enum class ArtifactsError {
    EMPTY,
    INVALID_TYPE
}