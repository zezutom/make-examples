package com.tomaszezula.make.server.model.domain.scenarios

sealed interface Scheduling {
    fun toJson(): String
}

data class IndefiniteScheduling(val interval: Int) : Scheduling {
    override fun toJson(): String =
        """
            {"type":"indefinitely","interval":$interval}            
        """.trimIndent()
}
