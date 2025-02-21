package com.data.repository.course

import com.data.api.ApiService
import com.data.model.base.Result
import com.data.model.course.CourseEntity
import com.data.model.course.add.AddCourseRequest
import com.data.model.course.get.GetAllCoursesRequest
import com.data.safeCall
import java.util.UUID

class CourseRepository(private val apiService: ApiService) : ICourseRepository {

    override suspend fun addCourse(request: AddCourseRequest): Result<CourseEntity> {
        return safeCall(request = { apiService.addCourse(request) })
    }

    override suspend fun getCourseById(id: UUID): Result<CourseEntity> {
        return safeCall(request = { apiService.getCourseById(id.toString()) })
    }

    override suspend fun getAllCoursesForUser(request: GetAllCoursesRequest): Result<List<CourseEntity>> {
        return safeCall(request = { apiService.getAllCourses(request) })
    }

}