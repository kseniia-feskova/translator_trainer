package data.repository.course

import data.model.base.Result
import data.model.course.CourseEntity
import data.model.course.add.AddCourseRequest

interface ICourseRepository {

    suspend fun addCourse(request: AddCourseRequest): Result<CourseEntity>

    suspend fun getCourseById(id: String): Result<CourseEntity>

    suspend fun getAllCoursesForUser(userId: String): Result<List<CourseEntity>>

}