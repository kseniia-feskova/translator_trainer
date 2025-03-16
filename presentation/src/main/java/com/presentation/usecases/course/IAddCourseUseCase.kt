package com.presentation.usecases.course

import com.presentation.model.CourseUI

interface IAddCourseUseCase {

    suspend fun invoke(course: CourseUI, needToCreateCourse: Boolean): Result<CourseUI>
}