package presentation.usecases.course

import presentation.model.CourseUI

interface IAddCourseUseCase {

    suspend fun invoke(course: CourseUI, needToCreateCourse: Boolean): Result<CourseUI>
}