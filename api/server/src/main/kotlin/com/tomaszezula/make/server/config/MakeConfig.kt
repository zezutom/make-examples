package com.tomaszezula.make.server.config

import com.tomaszezula.make.server.model.web.AuthToken
import java.net.URI

data class MakeConfig(val baseUrl: URI, val token: AuthToken)
