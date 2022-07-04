package com.tomaszezula.make.model.web

sealed interface ApiResponse

@kotlinx.serialization.Serializable
data class CreateScenarioResponse(val scenarioId: Long) : ApiResponse
