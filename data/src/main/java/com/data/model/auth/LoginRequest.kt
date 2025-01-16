package com.data.model.auth

data class LoginRequest(
    val email: String,
    val username: String,
    val password: String,
)