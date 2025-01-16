package com.data.model

import java.util.UUID

data class UserEntity(
    val id: UUID,
    val email: String,
    val username: String? = null,
    val photoUrl: String? = null,
    val courses: List<UUID> = emptyList()
)
