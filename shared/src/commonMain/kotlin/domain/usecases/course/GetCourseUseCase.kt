package domain.usecases.course

import data.model.course.CourseEntity
import data.repository.ICourseRepository
import domain.token.ICheckToken
import domain.token.ITokenRefresher

class GetCourseUseCase(
    private val repo: ICourseRepository,
    private val tokenRefresher: ITokenRefresher,
    private val checkToken: ICheckToken
) : IGetCourseUseCase {

    override suspend fun invoke(courseId: String): Result<CourseEntity> {
        val response = checkToken.safeApiCallWithRefresh(
            call = { repo.getCourseById(courseId) },
            onTokenExpired = { tokenRefresher.refreshToken() })
        val data = response.data
        return if (response.errorMsg.isNotEmpty()) {
            Result.failure(Exception(response.errorMsg))
        } else if (data == null) {
            Result.failure(Exception("Empty user data"))
        } else Result.success(data)
    }

}