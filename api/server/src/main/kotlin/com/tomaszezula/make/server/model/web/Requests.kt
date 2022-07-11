package com.tomaszezula.make.server.model.web

import com.tomaszezula.make.server.model.domain.Command
import com.tomaszezula.make.server.model.domain.CreateScenarioCommand
import com.tomaszezula.make.server.model.domain.GetScenarioBlueprintCommand
import com.tomaszezula.make.server.model.domain.organisations.TeamId
import com.tomaszezula.make.server.model.domain.scenarios.Blueprint
import com.tomaszezula.make.server.model.domain.scenarios.FolderId
import com.tomaszezula.make.server.model.domain.scenarios.IndefiniteScheduling
import com.tomaszezula.make.server.model.domain.scenarios.ScenarioId
import java.util.Base64

sealed interface Request<T : Command> {
    fun toCommand(): T
}

@kotlinx.serialization.Serializable
data class CreateScenarioRequest(
    val blueprint: String,
    val schedulingType: SchedulingType,
    val schedulingInterval: Int,
    val folderId: Long,
    val teamId: Long
) : Request<CreateScenarioCommand> {
    enum class SchedulingType {
        INDEFINITE
    }

    override fun toCommand(): CreateScenarioCommand =
        CreateScenarioCommand(
            Blueprint(String(Base64.getDecoder().decode(this.blueprint))),
            when (this.schedulingType) {
                SchedulingType.INDEFINITE -> IndefiniteScheduling(this.schedulingInterval)
            },
            FolderId(this.folderId),
            TeamId(this.teamId)
        )

}

@kotlinx.serialization.Serializable
data class GetScenarioBlueprintRequest(
    val scenarioId: Long,
    val draft: Boolean?
) : Request<GetScenarioBlueprintCommand> {
    override fun toCommand(): GetScenarioBlueprintCommand =
        GetScenarioBlueprintCommand(ScenarioId(this.scenarioId), this.draft ?: false)
}
