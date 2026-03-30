package domain.usecases.course

import data.model.course.CourseEntity

interface IGetCourseUseCase {

    suspend fun invoke(courseId: String): Result<CourseEntity>

}