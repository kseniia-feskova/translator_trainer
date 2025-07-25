package data.repository

import data.model.base.Result
import data.model.course.CourseEntity
import data.model.course.add.AddCourseRequest
import data.model.course.get.GetAllCoursesRequest

interface ICourseRepository {

    suspend fun addCourse(request: AddCourseRequest): Result<CourseEntity>

    suspend fun getCourseById(id: String): Result<CourseEntity>

    suspend fun getAllCoursesForUser(request: GetAllCoursesRequest): Result<List<CourseEntity>>

}