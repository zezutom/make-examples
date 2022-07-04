package com.tomaszezula.make.plugins

import com.tomaszezula.make.handlers.CreateScenarioHandler
import com.tomaszezula.make.handlers.Handler
import com.tomaszezula.make.model.domain.Command
import com.tomaszezula.make.model.web.CreateScenarioRequest
import com.tomaszezula.make.model.web.Request
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
