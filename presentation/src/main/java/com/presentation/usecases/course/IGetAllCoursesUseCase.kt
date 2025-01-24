package com.presentation.usecases.course

import com.presentation.model.CourseUI
import java.util.UUID

interface IGetAllCoursesUseCase {
    suspend fun invoke(userId: UUID): Result<List<CourseUI>>
}