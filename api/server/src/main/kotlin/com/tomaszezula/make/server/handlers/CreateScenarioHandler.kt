package com.tomaszezula.make.server.handlers

import com.fasterxml.jackson.databind.ObjectMapper
import com.tomaszezula.make.api.Scenario__1
import com.tomaszezula.make.server.config.MakeConfig
import com.tomaszezula.make.server.handlers.CreateScenarioHandler.Companion.Keys.BlueprintKey
import com.tomaszezula.make.server.handlers.CreateScenarioHandler.Companion.Keys.FolderIdKey
import com.tomaszezula.make.server.handlers.CreateScenarioHandler.Companion.Keys.SchedulingKey
import com.tomaszezula.make.server.handlers.CreateScenarioHandler.Companion.Keys.TeamIdKey
import com.tomaszezula.make.server.model.domain.CreateScenarioCommand
import com.tomaszezula.make.server.model.web.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class CreateScenarioHandler(
    private val client: HttpClient,
    private val objectMapper: ObjectMapper,
    private val config: MakeConfig
) : Handler<CreateScenarioCommand> {
    companion object {
        object Keys {
            const val BlueprintKey = "blueprint"
            const val SchedulingKey = "scheduling"
            const val FolderIdKey = "folderId"
            const val TeamIdKey = "teamId"
        }
    }

    private val requestUrl = "${config.baseUrl}/scenarios?confirmed=true"

    override suspend fun handle(request: CreateScenarioCommand): Response {
        val response = client.post(requestUrl) {
            headers {
                append(HttpHeaders.Authorization, "Token ${config.token.value}")
            }
            contentType(ContentType.Application.Json)
            setBody(
                buildJsonObject {
                    put(BlueprintKey, request.blueprint.value.lineSequence().map { it.trim() }.joinToString(""))
                    put(SchedulingKey, request.scheduling.toJson())
                    put(FolderIdKey, request.folderId.value)
                    put(TeamIdKey, request.teamId.value)
                }.toString()
            )
        }
        return when (response.status) {
            HttpStatusCode.OK -> Created(response.toWeb())
            HttpStatusCode.BadRequest -> BadRequest(response.body())
            else -> Error(response.status, response.body())
        }
    }

    private suspend fun HttpResponse.toWeb(): CreateScenarioResponse {
        val scenario = objectMapper.readValue(this.bodyAsText(), Scenario__1::class.java)
        return CreateScenarioResponse(scenario.scenario.id)
    }
}