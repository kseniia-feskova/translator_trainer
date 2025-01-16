package com.presentation.usecases.course

import com.presentation.model.CourseUI
import java.util.UUID

interface IGetCourseUseCase {

    suspend fun invoke(courseId: UUID): Result<CourseUI>

}