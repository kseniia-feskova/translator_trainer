package domain.usecases.auth

import data.model.course.CourseEntity

interface ICreateFromGuestUseCase {

    suspend fun invoke(
        email: String,
        password: String,
        course: CourseEntity
    ): Result<String>
}