package com.tomaszezula.make.server.handlers

import com.tomaszezula.make.server.model.domain.Command
import com.tomaszezula.make.server.model.web.Response

interface Handler<T: Command> {
    suspend fun handle(request: T): Response
}