package com.data.model.auth

import java.util.UUID

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val uuid: UUID?
)