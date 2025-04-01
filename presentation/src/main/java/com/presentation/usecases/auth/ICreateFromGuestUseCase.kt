package com.presentation.usecases.auth

import com.presentation.model.CourseUI

interface ICreateFromGuestUseCase {

   suspend fun invoke(
        email: String,
        password: String,
        course: CourseUI
    )
}