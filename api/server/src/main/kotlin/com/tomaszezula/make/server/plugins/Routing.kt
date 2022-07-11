package com.tomaszezula.make.server.plugins

import com.tomaszezula.make.server.handlers.CreateScenarioHandler
import com.tomaszezula.make.server.handlers.GetScenarioBlueprintHandler
import com.tomaszezula.make.server.handlers.Handler
import com.tomaszezula.make.server.model.domain.Command
import com.tomaszezula.make.server.model.web.CreateScenarioRequest
import com.tomaszezula.make.server.model.web.Request
import com.tomaszezula.make.server.model.web.toGetScenarioBlueprintRequest
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

fun Application.configureRouting(
    createScenarioHandler: CreateScenarioHandler,
    getScenarioBlueprintHandler: GetScenarioBlueprintHandler
) {
    // Starting point for a Ktor app:
    routing {
        post("/scenarios") {
            createScenarioHandler.respond(CreateScenarioRequest::class).invoke(call)
        }
        get("/scenarios/{scenarioId}/blueprint") {
            getScenarioBlueprintHandler.respond(call.toGetScenarioBlueprintRequest()).invoke(call)
        }
    }
}

suspend inline fun <C : Command, T : Request<C>> Handler<C>.respond(clazz: KClass<T>): suspend (ApplicationCall) -> Unit =
    RouteResponder.respond(this, clazz, LoggerFactory.getLogger(this::class.java))

suspend inline fun <C : Command, T : Request<C>> Handler<C>.respond(request: T): suspend (ApplicationCall) -> Unit =
    RouteResponder.respond(this, request, LoggerFactory.getLogger(this::class.java))
