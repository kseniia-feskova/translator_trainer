package com.presentation.usecases.auth

import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface ILoginUseCase {
    suspend fun invoke(email: String, username: String, password: String): Result<UUID>

    fun listenUserId(): Flow<UUID?>
}

/*
*  при логине нужно проверить айди курса
*  если его нет, то показать диалог перед HomeScreen
*  взять список наборов для пользователя и выбрать из них один
*
* */