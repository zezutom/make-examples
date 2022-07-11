package com.tomaszezula.make.server

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.tomaszezula.make.server.config.MakeConfig
import com.tomaszezula.make.server.handlers.CreateScenarioHandler
import com.tomaszezula.make.server.handlers.GetScenarioBlueprintHandler
import com.tomaszezula.make.server.model.web.AuthToken
import com.tomaszezula.make.server.plugins.configureRouting
import com.typesafe.config.ConfigFactory
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.network.sockets.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import java.net.URI

fun main() {
    val objectMapper = ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .registerModule(KotlinModule.Builder().build())
    val config = HoconApplicationConfig(ConfigFactory.load())
    val client = HttpClient(CIO) {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
        install(HttpRequestRetry) {
            maxRetries = config.maxRetries()
            retryIf { _, response ->
                !response.status.isSuccess()
            }
            retryOnExceptionIf { _, cause ->
                cause.isTimeoutException()
            }
            exponentialDelay()
        }
    }
    val makeConfig = MakeConfig(
        baseUrl = URI(config.property("make.url").getString()),
        token = AuthToken(config.property("make.auth.token").getString())
    )
    embeddedServer(Netty, port = config.port, host = config.host) {
        install(ContentNegotiation) {
            json()
        }
        configureRouting(
            CreateScenarioHandler(client, objectMapper, makeConfig),
            GetScenarioBlueprintHandler(client, objectMapper, makeConfig)
        )
    }.start(wait = true)
}

private fun HoconApplicationConfig.maxRetries(): Int =
    this.propertyOrNull("ktor.client.retry.max")?.getString()?.toInt() ?: 5

private fun Throwable.isTimeoutException(): Boolean = when (this) {
    is HttpRequestTimeoutException -> true
    is ConnectTimeoutException -> true
    is SocketTimeoutException -> true
    else -> false
}

