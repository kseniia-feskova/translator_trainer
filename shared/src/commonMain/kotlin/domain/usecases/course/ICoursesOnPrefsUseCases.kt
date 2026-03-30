package domain.usecases.course

import data.model.course.CourseEntity
import kotlinx.coroutines.flow.Flow

interface ICoursesOnPrefsUseCases {

    suspend fun getAll(): List<CourseEntity>

    fun saveAll(courses: List<CourseEntity>)

    suspend fun saveOne(course: CourseEntity)

    suspend fun resetCourse()

    suspend fun getCourse(): CourseEntity?
    fun getCourseFlow(): Flow<CourseEntity?>

}