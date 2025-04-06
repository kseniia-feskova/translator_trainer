package translator.data.repository.course

import translator.data.model.base.Result
import translator.data.model.course.CourseEntity
import translator.data.model.course.add.AddCourseRequest
import translator.data.model.course.get.GetAllCoursesRequest
import java.util.UUID

interface ICourseRepository {

    suspend fun addCourse(request: AddCourseRequest): Result<CourseEntity>

    suspend fun getCourseById(id: UUID): Result<CourseEntity>

    suspend fun getAllCoursesForUser(request: GetAllCoursesRequest): Result<List<CourseEntity>>

}