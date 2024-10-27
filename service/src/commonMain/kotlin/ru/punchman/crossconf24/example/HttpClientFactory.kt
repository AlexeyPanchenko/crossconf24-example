package ru.punchman.crossconf24.example

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class HttpClientFactory(
    private val serializer: Json,
) {

    fun create(logLevel: LogLevel, timeout: Long): HttpClient {
        return HttpClient(CIO) {
            install(ContentNegotiation) {
                json(serializer)
            }
            install(Logging) {
                level = logLevel
                logger = object : Logger {
                    override fun log(message: String) {
                        println(message)
                    }
                }
                sanitizeHeader { header ->
                    header == HttpHeaders.Authorization
                }
            }
            this.expectSuccess = false
            install(HttpTimeout) {
                requestTimeoutMillis = timeout
                connectTimeoutMillis = timeout
                socketTimeoutMillis = timeout
            }
        }
    }
}