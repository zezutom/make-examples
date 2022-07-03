package com.tomaszezula.make

import com.tomaszezula.make.config.MakeConfig
import com.tomaszezula.make.handlers.CreateScenarioHandler
import com.tomaszezula.make.model.web.AuthToken
import com.tomaszezula.make.plugins.configureRouting
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import java.net.URI
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation as ClientContentNegotiation

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        install(ContentNegotiation) {
            json()
        }
        configureRouting(
            CreateScenarioHandler(
                HttpClient(CIO) {
                    install(Logging) {
                        logger = Logger.DEFAULT
                        level = LogLevel.ALL
                    }
//                    install(ClientContentNegotiation) {
//                        json(Json {
//                            ignoreUnknownKeys = true
//                        })
//                    }
                },
                MakeConfig(
                    baseUrl = URI("https://eu1.make.com/api/v2"),
                    token = AuthToken("710ac064-c371-4c3e-bb27-a0eb8d6aa98b")
                ),
                LoggerFactory.getLogger(CreateScenarioHandler::class.java)
            )
        )
    }.start(wait = true)
}
