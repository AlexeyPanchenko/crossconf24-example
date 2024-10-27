package ru.punchman.crossconf24.example

import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun main() {

    embeddedServer(CIO, port = 8080) {
        routing {
            get("/ping") {
                call.respondText("Hello!")
            }
        }
    }.start(wait = true)
}