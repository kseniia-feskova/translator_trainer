package data.prefs

import data.model.course.CourseEntity
import kotlinx.coroutines.flow.Flow

interface IDataStoreManager {
    fun listenUserId(): Flow<String?>

    suspend fun saveUserId(id: String?)
    suspend fun getUserId(): String?

    suspend fun saveCourse(course: CourseEntity?)
    suspend fun getCourse(): CourseEntity?

    fun saveCourses(courses: List<CourseEntity>)
    fun getCourses(): List<CourseEntity>

    suspend fun setGuestMode()
    suspend fun isGuest(): Boolean
    fun isGuestOnRuntime(): Boolean?
    suspend fun resetGuestMode()

    suspend fun saveEmail(email: String)
    suspend fun getEmail(): String?
}