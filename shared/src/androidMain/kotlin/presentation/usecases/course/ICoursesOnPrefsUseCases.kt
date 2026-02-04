package presentation.usecases.course

import kotlinx.coroutines.flow.Flow
import presentation.model.CourseUI

interface ICoursesOnPrefsUseCases {

    suspend fun getAll(): List<CourseUI>

    fun saveAll(courses: List<CourseUI>)

    suspend fun saveOne(course: CourseUI)

    suspend fun resetCourse()

    suspend fun getCourse(): CourseUI?
    fun getCourseFlow(): Flow<CourseUI?>

}