package com.tomaszezula.make.server.model.api

data class Transformer(
    val name: String,
    val label: String,
    val description: String,
    val expect: List<Spec>,
    val `interface`: List<Spec>,
    val output: Output
)
