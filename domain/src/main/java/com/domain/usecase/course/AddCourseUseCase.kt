package com.domain.usecase.course

import com.data.model.course.add.AddCourseRequest
import com.data.repository.course.ICourseRepository
import com.domain.mapper.toUI
import com.domain.token.ITokenRefresher
import com.domain.token.safeApiCallWithRefresh
import com.presentation.model.CourseUI
import com.presentation.usecases.course.IAddCourseUseCase
import java.util.UUID

class AddCourseUseCase(
    private val repo: ICourseRepository,
    private val tokenRefresher: ITokenRefresher
) : IAddCourseUseCase {

    override suspend fun invoke(userId: UUID, courseUI: CourseUI): Result<CourseUI> {
        val request = AddCourseRequest(
            name = "${courseUI.originalLanguage.name} - ${courseUI.translateLanguage.name}",
            userId = userId,
            sourceLanguage = courseUI.originalLanguage.code,
            targetLanguage = courseUI.translateLanguage.code
        )
        val response = safeApiCallWithRefresh(
            call = { repo.addCourse(request) },
            onTokenExpired = { tokenRefresher.refreshToken() }
        )
        val data = response.data
        return if (response.errorMsg.isNotEmpty()) {
            Result.failure(Exception(response.errorMsg))
        } else if (data == null) {
            Result.failure(Exception("Empty user data"))
        } else Result.success(data.toUI())
    }

}