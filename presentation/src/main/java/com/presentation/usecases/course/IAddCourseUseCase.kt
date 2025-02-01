package com.presentation.usecases.course

import com.presentation.model.CourseUI
import java.util.UUID

interface IAddCourseUseCase {

    suspend fun invoke(userId: UUID, courseUI: CourseUI): Result<CourseUI>
}