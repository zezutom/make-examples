package com.tomaszezula.make.server.model.api

data class Spec(
    val name: String,
    val label: String,
    val type: String,
    val help: String?,
    val required: Boolean?,
    val grouped: Boolean?,
    val multiline: Boolean?,
    val spec: List<Spec>?,
    val options: List<Options>?
)
