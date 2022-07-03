package com.tomaszezula.make.handlers

import com.tomaszezula.make.config.MakeConfig
import com.tomaszezula.make.handlers.CreateScenarioHandler.Companion.Keys.BlueprintKey
import com.tomaszezula.make.handlers.CreateScenarioHandler.Companion.Keys.FolderIdKey
import com.tomaszezula.make.handlers.CreateScenarioHandler.Companion.Keys.SchedulingKey
import com.tomaszezula.make.handlers.CreateScenarioHandler.Companion.Keys.TeamIdKey
import com.tomaszezula.make.handlers.CreateScenarioHandler.Companion.ResponseFields.Id
import com.tomaszezula.make.handlers.CreateScenarioHandler.Companion.ResponseFields.Scenario
import com.tomaszezula.make.model.domain.CreateScenarioCommand
import com.tomaszezula.make.model.web.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*
import org.slf4j.Logger

class CreateScenarioHandler(
    private val client: HttpClient,
    private val config: MakeConfig,
    private val logger: Logger
) : Handler<CreateScenarioCommand> {
    companion object {
        object Keys {
            const val BlueprintKey = "blueprint"
            const val SchedulingKey = "scheduling"
            const val FolderIdKey = "folderId"
            const val TeamIdKey = "teamId"
        }
        object ResponseFields {
            const val Scenario = "scenario"
            const val Id = "id"
        }
    }

    private val requestUrl = "${config.baseUrl}/scenarios?confirmed=true"

    override suspend fun handle(request: CreateScenarioCommand): Response {
        logger.debug("Received request: $request")
        val response = client.post(requestUrl) {
            headers {
                append(HttpHeaders.Authorization, "Token ${config.token.value}")
            }
            contentType(ContentType.Application.Json)
            setBody(
                buildJsonObject {
                    put(BlueprintKey, request.blueprint.json.lineSequence().map { it.trim() }.joinToString(""))
                    put(SchedulingKey, request.scheduling.toJson())
                    put(FolderIdKey, request.folderId.value)
                    put(TeamIdKey, request.teamId.value)
                }.toString()
            )
        }
        return when (response.status) {
            HttpStatusCode.OK -> Created(response.toApi())
            HttpStatusCode.BadRequest -> BadRequest(response.body())
            else -> Error(response.status, response.body())
        }
    }

    private suspend fun HttpResponse.toApi(): CreateScenarioResponse =
        CreateScenarioResponse(
            Json.decodeFromString<JsonObject>(this.body())[Scenario]!!.jsonObject[Id]!!.jsonPrimitive.long
        )
}