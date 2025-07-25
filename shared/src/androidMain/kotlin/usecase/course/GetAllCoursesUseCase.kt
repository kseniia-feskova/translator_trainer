package usecase.course

import data.model.course.get.GetAllCoursesRequest
import data.repository.ICourseRepository
import domain.token.ICheckToken
import domain.token.TokenRefresher
import mapper.toUI
import presentation.model.CourseUI
import presentation.usecases.course.IGetAllCoursesUseCase

class GetAllCoursesUseCase(
    private val repo: ICourseRepository,
    private val tokenRefresher: TokenRefresher,
    private val checkToken: ICheckToken
): IGetAllCoursesUseCase {

    override suspend fun invoke(userId: String): Result<List<CourseUI>> {
        val request = GetAllCoursesRequest(userId)
        val response = checkToken.safeApiCallWithRefresh(
            call = { repo.getAllCoursesForUser(request) },
            onTokenExpired = { tokenRefresher.refreshToken() }
        )
        val data = response.data
        return if (response.errorMsg.isNotEmpty()) {
            Result.failure(Exception(response.errorMsg))
        } else if (data == null) {
            Result.failure(Exception("Empty user data"))
        } else Result.success(data.map { it.toUI() })
    }

}