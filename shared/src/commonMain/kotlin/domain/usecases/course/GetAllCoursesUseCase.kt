package domain.usecases.course

import data.model.course.CourseEntity
import data.repository.ICourseRepository
import domain.token.ICheckToken
import domain.token.TokenRefresher

class GetAllCoursesUseCase(
    private val repo: ICourseRepository,
    private val tokenRefresher: TokenRefresher,
    private val checkToken: ICheckToken
): IGetAllCoursesUseCase {

    override suspend fun invoke(userId: String): Result<List<CourseEntity>> {
        val response = checkToken.safeApiCallWithRefresh(
            call = { repo.getAllCoursesForUser(userId) },
            onTokenExpired = { tokenRefresher.refreshToken() }
        )
        val data = response.data
        return if (response.errorMsg.isNotEmpty()) {
            Result.failure(Exception(response.errorMsg))
        } else if (data == null) {
            Result.failure(Exception("Empty user data"))
        } else Result.success(data)
    }

}