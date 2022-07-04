package com.tomaszezula.make.model.domain

import com.tomaszezula.make.model.domain.organisations.TeamId
import com.tomaszezula.make.model.domain.scenarios.Blueprint
import com.tomaszezula.make.model.domain.scenarios.FolderId
import com.tomaszezula.make.model.domain.scenarios.Scheduling

sealed interface Command

data class CreateScenarioCommand(
    val blueprint: Blueprint,
    val scheduling: Scheduling,
    val folderId: FolderId,
    val teamId: TeamId
) : Command
