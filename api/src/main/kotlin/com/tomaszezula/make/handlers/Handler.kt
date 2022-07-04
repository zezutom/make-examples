package com.tomaszezula.make.handlers

import com.tomaszezula.make.model.domain.Command
import com.tomaszezula.make.model.web.Response

interface Handler<T: Command> {
    suspend fun handle(request: T): Response
}