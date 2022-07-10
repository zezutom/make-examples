package com.tomaszezula.make.server.model.api

import java.net.URI

data class Trigger(
    val name: String,
    val label: String,
    val description: String,
    val parameters: List<Parameter>,
    val samples: URI,
    val `interface`: URI,
    val output: Output
)

