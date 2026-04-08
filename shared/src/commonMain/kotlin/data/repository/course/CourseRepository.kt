package data.repository.course

import data.api.ApiService
import data.model.base.Result
import data.model.course.CourseEntity
import data.model.course.add.AddCourseRequest
import data.repository.safeCall

class CourseRepository(private val apiService: ApiService) : ICourseRepository {

    override suspend fun addCourse(request: AddCourseRequest): Result<CourseEntity> {
        return safeCall(request = { apiService.addCourse(request) })
    }

    override suspend fun getCourseById(id: String): Result<CourseEntity> {
        return safeCall(request = { apiService.getCourseById(id) })
    }

    override suspend fun getAllCoursesForUser(userId: String): Result<List<CourseEntity>> {
        return safeCall(request = { apiService.getAllCourses(userId) })
    }

}