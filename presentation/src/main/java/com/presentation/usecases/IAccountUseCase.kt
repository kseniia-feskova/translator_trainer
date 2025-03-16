package com.presentation.usecases

import java.util.UUID

interface IAccountUseCase {

    suspend fun getUserId(): UUID?
}