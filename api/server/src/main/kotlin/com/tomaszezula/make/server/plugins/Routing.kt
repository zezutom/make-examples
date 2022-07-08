package com.tomaszezula.make.server.plugins

import com.tomaszezula.make.server.handlers.CreateScenarioHandler
import com.tomaszezula.make.server.handlers.Handler
import com.tomaszezula.make.server.model.domain.Command
import com.tomaszezula.make.server.model.web.CreateScenarioRequest
import com.tomaszezula.make.server.model.web.Request
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

fun Application.configureRouting(
    createScenarioHandler: CreateScenarioHandler
) {
    // Starting point for a Ktor app:
    routing {
        post("/scenarios") {
            createScenarioHandler.respondTo(CreateScenarioRequest::class).invoke(call)
        }
    }
}

suspend inline fun <C : Command, T : Request<C>> Handler<C>.respondTo(clazz: KClass<T>): suspend (ApplicationCall) -> Unit =
    RouteResponder.respond(this, clazz, LoggerFactory.getLogger(this::class.java))
