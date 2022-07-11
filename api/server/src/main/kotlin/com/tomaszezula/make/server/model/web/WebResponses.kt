package com.tomaszezula.make.server.model.web

sealed interface WebResponse

@kotlinx.serialization.Serializable
data class CreateScenarioResponse(val scenarioId: Long) : WebResponse

@kotlinx.serialization.Serializable
data class GetScenarioBlueprintResponse(val name: String, val flows: List<Flow>, val json: String) : WebResponse {
    @kotlinx.serialization.Serializable
    data class Flow(val id: Int, val module: String)
}