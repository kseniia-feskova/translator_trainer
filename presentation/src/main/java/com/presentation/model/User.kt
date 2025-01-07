package com.presentation.model

import java.util.UUID

data class User(
    val userId: UUID,
    val username: String,
    val email: String,
    val photo: String? = null
)
