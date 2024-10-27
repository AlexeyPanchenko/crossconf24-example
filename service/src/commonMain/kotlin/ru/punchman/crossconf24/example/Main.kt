package ru.punchman.crossconf24.example

import io.ktor.client.plugins.logging.LogLevel
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json
import ru.punchman.crossconf24.example.controller.ReleaseController
import ru.punchman.crossconf24.example.data.ReleaseRepository
import ru.punchman.crossconf24.example.data.ReleaseStorage
import ru.punchman.crossconf24.example.data.RuStoreApi
import ru.punchman.crossconf24.example.domain.FilesValidator
import ru.punchman.crossconf24.example.domain.ReleaseService

fun main() {

    val json = Json {
        encodeDefaults = false
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    val httpClient = HttpClientFactory(json).create(LogLevel.ALL, timeout = 30_000L)

    val controller = ReleaseController(
        ReleaseService(
            FilesValidator(),
            ReleaseRepository(
                RuStoreApi(httpClient),
                ReleaseStorage()
            )
        )
    )

    embeddedServer(CIO, port = 8080) {
        routing {
            install(ContentNegotiation) {
                json(json)
            }
            get("/ping") {
                call.respondText("Ok")
            }
            route("/api/v1") {
                controller.configure(this)
            }
        }
    }.start(wait = true)
}