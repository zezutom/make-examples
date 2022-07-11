package com.tomaszezula.make.server.model.domain

import com.tomaszezula.make.server.model.domain.organisations.TeamId
import com.tomaszezula.make.server.model.domain.scenarios.Blueprint
import com.tomaszezula.make.server.model.domain.scenarios.FolderId
import com.tomaszezula.make.server.model.domain.scenarios.ScenarioId
import com.tomaszezula.make.server.model.domain.scenarios.Scheduling

sealed interface Command

data class CreateScenarioCommand(
    val blueprint: Blueprint,
    val scheduling: Scheduling,
    val folderId: FolderId,
    val teamId: TeamId
) : Command

data class GetScenarioBlueprintCommand(
    val scenarioId: ScenarioId,
    val draft: Boolean
) : Command