package ru.punchman.crossconf24.example.data

import arrow.core.Either
import arrow.core.raise.either
import io.ktor.client.HttpClient
import ru.punchman.crossconf24.example.controller.Release
import ru.punchman.crossconf24.example.controller.ReleaseStatus

// Stub implementation with random result
class RuStoreApi(
    private val httpClient: HttpClient
) {

    suspend fun createRelease(appId: String, version: String, files: List<String>): Either<StoreApiError, Release> =
        either {
            val random = (0..100).random()
            when (random) {
                in 0..50 -> Release("12345", version, ReleaseStatus.DRAFT)
                in 51..70 -> raise(
                    StoreApiError.Forbidden("Create release not allowed for this api key. Please grand permissions")
                )

                else -> raise(
                    StoreApiError.InvalidMetadata("App $appId already in progress. You can't create new release")
                )
            }
        }
}

sealed interface StoreApiError {
    class Forbidden(val details: String) : StoreApiError
    class InvalidMetadata(val details: String) : StoreApiError
}