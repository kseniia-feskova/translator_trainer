package com.data.model.auth

data class AuthRequest(
    val username: String,
    val phone: String? = null,
    val email: String? = null,
    val password: String
)