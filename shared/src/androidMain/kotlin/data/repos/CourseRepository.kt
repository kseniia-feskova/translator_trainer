package data.repos

import data.api.ApiService
import data.model.course.CourseEntity
import data.model.course.get.GetAllCoursesRequest
import data.repository.ICourseRepository
import data.safeCall
import data.model.base.Result

class CourseRepository(private val apiService: ApiService) : ICourseRepository {

    override suspend fun addCourse(request: data.model.course.add.AddCourseRequest): Result<CourseEntity> {
        return safeCall(request = { apiService.addCourse(request) })
    }

    override suspend fun getCourseById(id: String): Result<CourseEntity> {
        return safeCall(request = { apiService.getCourseById(id) })
    }

    override suspend fun getAllCoursesForUser(request: GetAllCoursesRequest): Result<List<CourseEntity>> {
        return safeCall(request = { apiService.getAllCourses(request) })
    }

}