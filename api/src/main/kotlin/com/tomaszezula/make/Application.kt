package com.tomaszezula.make

import com.tomaszezula.make.config.MakeConfig
import com.tomaszezula.make.handlers.CreateScenarioHandler
import com.tomaszezula.make.model.web.AuthToken
import com.tomaszezula.make.plugins.configureRouting
import com.typesafe.config.ConfigFactory
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import org.slf4j.LoggerFactory
import java.net.URI

fun main() {
    val config = HoconApplicationConfig(ConfigFactory.load())
    embeddedServer(Netty, port = config.port, host = config.host) {
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
                },
                MakeConfig(
                    baseUrl = URI(config.property("make.url").getString()),
                    token = AuthToken(config.property("make.auth.token").getString())
                ),
                LoggerFactory.getLogger(CreateScenarioHandler::class.java)
            )
        )
    }.start(wait = true)
}
