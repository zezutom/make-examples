package com.tomaszezula.make.server.model.web

sealed interface ApiResponse

@kotlinx.serialization.Serializable
data class CreateScenarioResponse(val scenarioId: Long) : ApiResponse
