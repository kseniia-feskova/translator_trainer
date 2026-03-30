package domain.usecases.course

import data.model.course.CourseEntity

interface IAddCourseUseCase {

    suspend fun invoke(course: CourseEntity, needToCreateCourse: Boolean): Result<CourseEntity>
}