package com.data.repository.course

import com.data.api.ApiService
import com.data.api.Result
import com.data.model.course.CourseEntity
import com.data.repository.user.ICourseRepository
import com.data.safeCall
import java.util.UUID

class CourseRepository(private val apiService: ApiService) : ICourseRepository {
    override suspend fun getCourseById(id: UUID): Result<CourseEntity> {
        return safeCall(request = { apiService.getCourseById(id.toString()) })
    }
}