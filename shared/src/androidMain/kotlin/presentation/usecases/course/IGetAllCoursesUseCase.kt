package presentation.usecases.course

import presentation.model.CourseUI

interface IGetAllCoursesUseCase {
    suspend fun invoke(userId: String): Result<List<CourseUI>>
}