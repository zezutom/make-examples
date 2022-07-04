package com.tomaszezula.make.model.domain.scenarios

sealed interface Scheduling {
    fun toJson(): String
}

data class IndefiniteScheduling(val interval: Int) : Scheduling {
    override fun toJson(): String =
        """
            {"type":"indefinitely","interval":$interval}            
        """.trimIndent()
}
