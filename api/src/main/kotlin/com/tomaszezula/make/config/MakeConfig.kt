package com.tomaszezula.make.config

import com.tomaszezula.make.model.web.AuthToken
import java.net.URI

data class MakeConfig(val baseUrl: URI, val token: AuthToken)
