package com.tomaszezula.make.plugins

import com.tomaszezula.make.handlers.Handler
import com.tomaszezula.make.model.domain.Command
import com.tomaszezula.make.model.web.*
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
        try {
            val request = call.receive(clazz)
            when (val response = handler.handle(request.toCommand())) {
                is Created -> call.respond(HttpStatusCode.Created, response.value)
                is BadRequest -> call.respond(HttpStatusCode.BadRequest, response.message)
                is Error -> call.respond(response.statusCode, response.message)
            }
        } catch (e: Exception) {
            logger.warn("Request failed", e)
            call.respond(HttpStatusCode.InternalServerError, "Request failed")
        }
    }
}
