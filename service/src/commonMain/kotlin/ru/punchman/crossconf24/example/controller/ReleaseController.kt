package ru.punchman.crossconf24.example.controller

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.RoutingContext
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import ru.punchman.crossconf24.example.data.StoreApiError
import ru.punchman.crossconf24.example.domain.ArtifactsError
import ru.punchman.crossconf24.example.domain.ReleaseError
import ru.punchman.crossconf24.example.domain.ReleaseService

private const val APP_ID = "appId"

class ReleaseController(
    private val service: ReleaseService
) {

    fun configure(route: Route) {
        route.route("/apps/{${APP_ID}}") {
            post("/release") { createRelease() }
        }
    }

    private suspend fun RoutingContext.createRelease() {
        try {
            val appId = call.pathParameters[APP_ID]
                ?: return call.respond(HttpStatusCode.BadRequest, "$APP_ID not found from path patram")
            val request = call.receive<CreateReleaseRequest>()
            service.createRelease(appId, request)
                .onLeft { error -> responseError(error) }
                .onRight { call.respond(it) }
        } catch (e: Exception) {
            e.printStackTrace()
            call.respond(
                HttpStatusCode.InternalServerError, "System error! $e"
            )
        }
    }

    private suspend fun RoutingContext.responseError(error: ReleaseError) {
        when (error) {
            is ReleaseError.Duplicate ->
                call.respond(HttpStatusCode.Conflict, "Release ${error.version} already exists")
            is ReleaseError.InvalidArtifacts -> when (error.error) {
                ArtifactsError.EMPTY ->
                    call.respond(HttpStatusCode.BadRequest, "At least one file required for new release")
                ArtifactsError.INVALID_TYPE ->
                    call.respond(HttpStatusCode.BadRequest, "Invalid file type. Release required only .apk files")
            }

            is ReleaseError.StoreError -> when (val storeError = error.error) {
                is StoreApiError.Forbidden ->
                    call.respond(HttpStatusCode.Forbidden, "Store access denied! ${storeError.details}")
                is StoreApiError.InvalidMetadata ->
                    call.respond(
                        HttpStatusCode.BadRequest, "Invalid data for new release. ${storeError.details}"
                    )
            }
        }
    }
}

