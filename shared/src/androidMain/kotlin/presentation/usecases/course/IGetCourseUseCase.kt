package presentation.usecases.course

import presentation.model.CourseUI

interface IGetCourseUseCase {

    suspend fun invoke(courseId: String): Result<CourseUI>

}