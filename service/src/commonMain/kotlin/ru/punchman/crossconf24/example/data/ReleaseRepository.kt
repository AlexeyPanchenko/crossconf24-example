package ru.punchman.crossconf24.example.data

import arrow.core.Either
import arrow.core.raise.either
import ru.punchman.crossconf24.example.controller.Release
import ru.punchman.crossconf24.example.ensure

class ReleaseRepository(
    private val api: RuStoreApi,
    private val storage: ReleaseStorage
) {

    suspend fun getRelease(appId: String, version: String): Release? {
        return storage.getRelease(appId, version)
    }

    suspend fun createRelease(
        appId: String,
        version: String,
        files: List<String>
    ): Either<RepositoryError, Release> = either {
        val release = ensure(api.createRelease(appId, version, files)) {
            RepositoryError(it)
        }
        storage.createRelease(appId, release)
        release
    }
}

class RepositoryError(val error: StoreApiError)

