package com.presentation.model

import java.util.UUID

data class UserUI(
    val userId: UUID,
    val username: String,
    val email: String,
    val photo: String? = null,
    val courses: List<UUID> = emptyList()
)
