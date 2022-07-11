package com.tomaszezula.make.server.model.web

import io.ktor.http.*


sealed interface Response

data class OK(val value: Any) : Response

data class Created(val value: Any) : Response

data class BadRequest(val message: String) : Response

data class Error(val statusCode: HttpStatusCode, val message: String) : Response

