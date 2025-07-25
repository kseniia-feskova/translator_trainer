package presentation.usecases.auth

import presentation.model.CourseUI

interface ICreateFromGuestUseCase {

    suspend fun invoke(
        email: String,
        password: String,
        course: CourseUI
    ): Result<String>
}