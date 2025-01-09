package com.domain.token

interface ITokenRefresher {
    suspend fun refreshToken():Boolean
}