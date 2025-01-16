package com.data.model.auth

data class AuthRequest(
    val email: String,
    val username: String,
    val password: String,
    val originalLanguage: String,
    val translateLanguage: String
)