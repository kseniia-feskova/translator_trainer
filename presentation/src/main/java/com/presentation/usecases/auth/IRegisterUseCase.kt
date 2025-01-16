package com.presentation.usecases.auth

import com.presentation.utils.Language
import java.util.UUID

interface IRegisterUseCase {
    suspend fun invoke(
        email: String,
        username: String,
        password: String,
        originalLanguage: Language,
        translateLanguage: Language
    ): Result<UUID?>
}