package com.tomaszezula.make.server.model.api

data class App(
    val name: String,
    val version: String,
    val author: String,
    val label: String,
    val theme: String,
    val description: String,
    val keywords: String,
    val triggers: List<Trigger>?,
    val transformers: List<Transformer>?
)
