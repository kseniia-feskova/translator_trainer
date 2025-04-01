package com.presentation.usecases.auth

import com.presentation.model.CourseUI
import java.util.UUID

interface ICreateFromGuestUseCase {

    suspend fun invoke(
        email: String,
        password: String,
        course: CourseUI
    ): Result<UUID>
}