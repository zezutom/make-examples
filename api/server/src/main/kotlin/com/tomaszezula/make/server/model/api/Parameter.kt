package com.tomaszezula.make.server.model.api

data class Parameter(
    val name: String,
    val label: String,
    val type: String,
    val spec: List<Spec>,
    val required: Boolean
)
