package domain.usecases.course

import data.model.course.CourseEntity

interface IGetAllCoursesUseCase {
    suspend fun invoke(userId: String): Result<List<CourseEntity>>
}