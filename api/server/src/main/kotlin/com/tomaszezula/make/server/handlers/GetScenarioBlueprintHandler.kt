package com.tomaszezula.make.server.handlers

import com.fasterxml.jackson.databind.ObjectMapper
import com.tomaszezula.make.api.Blueprint
import com.tomaszezula.make.api.Flow
import com.tomaszezula.make.server.config.MakeConfig
import com.tomaszezula.make.server.model.domain.GetScenarioBlueprintCommand
import com.tomaszezula.make.server.model.web.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class GetScenarioBlueprintHandler(
    private val client: HttpClient,
    private val objectMapper: ObjectMapper,
    private val config: MakeConfig
) : Handler<GetScenarioBlueprintCommand> {

    override suspend fun handle(request: GetScenarioBlueprintCommand): Response {
        val response = client.get("${config.baseUrl}/scenarios/${request.scenarioId.value}/blueprint") {
            headers {
                append(HttpHeaders.Authorization, "Token ${config.token.value}")
            }
            contentType(ContentType.Application.Json)
        }
        return when (response.status) {
            HttpStatusCode.OK -> OK(response.toWeb())
            HttpStatusCode.BadRequest -> BadRequest(response.body())
            else -> Error(response.status, response.body())
        }
    }

    private suspend fun HttpResponse.toWeb(): GetScenarioBlueprintResponse {
        val json = this.bodyAsText()
        val blueprint = objectMapper.readValue(json, Blueprint::class.java)
        return GetScenarioBlueprintResponse(
            blueprint.response.blueprint.name,
            blueprint.response.blueprint.flow.map { it.toWeb() },
            json
        )
    }

    private fun Flow.toWeb(): GetScenarioBlueprintResponse.Flow =
        GetScenarioBlueprintResponse.Flow(
            this.id,
            this.module.toString()
        )
}