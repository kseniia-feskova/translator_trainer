package com.domain.usecase.course

import com.data.model.course.get.GetAllCoursesRequest
import com.data.repository.course.ICourseRepository
import com.domain.mapper.toUI
import com.domain.token.TokenRefresher
import com.domain.token.safeApiCallWithRefresh
import com.presentation.model.CourseUI
import com.presentation.usecases.course.IGetAllCoursesUseCase
import java.util.UUID

class GetAllCoursesUseCase(
    private val repo: ICourseRepository,
    private val tokenRefresher: TokenRefresher
) : IGetAllCoursesUseCase {

    override suspend fun invoke(userId: UUID): Result<List<CourseUI>> {
        val request = GetAllCoursesRequest(userId)
        val response = safeApiCallWithRefresh(
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