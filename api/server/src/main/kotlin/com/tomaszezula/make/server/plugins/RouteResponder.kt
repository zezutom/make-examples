package com.tomaszezula.make.server.plugins

import com.tomaszezula.make.server.handlers.Handler
import com.tomaszezula.make.server.model.domain.Command
import com.tomaszezula.make.server.model.web.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.slf4j.Logger
import kotlin.reflect.KClass

object RouteResponder {

    suspend fun <C : Command, T : Request<C>> respond(
        handler: Handler<C>,
        clazz: KClass<T>,
        logger: Logger
    ): suspend (ApplicationCall) -> Unit = { call ->
        respond(handler, call.receive(clazz), logger).invoke(call)
    }

    suspend fun <C : Command, T : Request<C>> respond(
        handler: Handler<C>,
        request: T,
        logger: Logger
    ): suspend (ApplicationCall) -> Unit = { call ->
        runSuspendCatching {
            when (val response = handler.handle(request.toCommand())) {
                is OK -> call.respond(HttpStatusCode.OK, response.value)
                is Created -> call.respond(HttpStatusCode.Created, response.value)
                is BadRequest -> call.respond(HttpStatusCode.BadRequest, response.message)
                is Error -> call.respond(response.statusCode, response.message)
            }
        }.onFailure { e ->
            logger.warn("Request failed", e)
            call.respond(HttpStatusCode.InternalServerError, "Request failed")
        }
    }
}
