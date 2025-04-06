package translator.data.repository.course

import translator.data.repository.course.ICourseRepository
import translator.data.api.ApiService
import translator.data.model.base.Result
import translator.data.model.course.CourseEntity
import translator.data.model.course.add.AddCourseRequest
import translator.data.model.course.get.GetAllCoursesRequest
import translator.data.safeCall
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