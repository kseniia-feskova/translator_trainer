package com.domain.usecase.course

import com.data.repository.user.ICourseRepository
import com.domain.mapper.toUI
import com.domain.token.ITokenRefresher
import com.domain.token.safeApiCallWithRefresh
import com.presentation.model.CourseUI
import com.presentation.usecases.course.IGetCourseUseCase
import java.util.UUID

class GetCourseUseCase(
    private val repo: ICourseRepository,
    private val tokenRefresher: ITokenRefresher
) : IGetCourseUseCase {
    override suspend fun invoke(courseId: UUID): Result<CourseUI> {
        val response = safeApiCallWithRefresh(
            call = { repo.getCourseById(courseId) },
            onTokenExpired = { tokenRefresher.refreshToken() })
        val data = response.data
        return if (response.errorMsg.isNotEmpty()) {
            Result.failure(Exception(response.errorMsg))
        } else if (data == null) {
            Result.failure(Exception("Empty user data"))
        } else Result.success(data.toUI())
    }

}