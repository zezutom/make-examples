package com.tomaszezula.make.server.model.web

import io.ktor.server.application.*

object RequestParams {
    const val ScenarioId = "scenarioId"
    const val Draft = "draft"
}
fun ApplicationCall.toGetScenarioBlueprintRequest(): GetScenarioBlueprintRequest =
    GetScenarioBlueprintRequest(
        this.parameters[RequestParams.ScenarioId]!!.toLong(),
        this.request.queryParameters[RequestParams.Draft]?.toBoolean() ?: false
    )