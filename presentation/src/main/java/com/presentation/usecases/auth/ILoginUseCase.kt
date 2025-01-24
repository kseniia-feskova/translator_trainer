package com.presentation.usecases.auth

import java.util.UUID

interface ILoginUseCase {
    suspend fun invoke(email: String, username: String, password: String): Result<UUID?>
}

/*
*  при логине нужно проверить айди курса
*  если его нет, то показать диалог перед HomeScreen
*  взять список наборов для пользователя и выбрать из них один
*
* */