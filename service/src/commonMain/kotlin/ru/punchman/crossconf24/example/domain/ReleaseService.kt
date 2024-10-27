package ru.punchman.crossconf24.example.domain

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import ru.punchman.crossconf24.example.controller.CreateReleaseRequest
import ru.punchman.crossconf24.example.controller.Release
import ru.punchman.crossconf24.example.data.ReleaseRepository
import ru.punchman.crossconf24.example.data.StoreApiError
import ru.punchman.crossconf24.example.ensure

class ReleaseService(
    private val filesValidator: FilesValidator,
    private val repository: ReleaseRepository
) {

    suspend fun createRelease(appId: String, request: CreateReleaseRequest): Either<ReleaseError, Release> = either {
        val existedRelease = repository.getRelease(appId, request.version)
        ensure(existedRelease == null) {
            ReleaseError.Duplicate(existedRelease!!.version)
        }
        ensure(filesValidator.validate(request.files)) {
            ReleaseError.InvalidArtifacts(it)
        }
        ensure(repository.createRelease(appId, request.version, request.files)) {
            ReleaseError.StoreError(it.error)
        }
    }
}

sealed interface ReleaseError {
    class Duplicate(val version: String) : ReleaseError
    class InvalidArtifacts(val error: ArtifactsError) : ReleaseError
    class StoreError(val error: StoreApiError) : ReleaseError
}