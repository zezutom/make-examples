package com.tomaszezula.make.server.model.api

data class Options(val label: String, val options: List<Option>) {
    data class Option(val label: String, val value: String)
}
