package data.prefs

import data.model.course.CourseEntity

// simple local data storage
interface IDataStoreManager {

    suspend fun saveUserId(id: String?)
    suspend fun getUserId(): String?

    suspend fun saveCourse(course: CourseEntity?)
    suspend fun getCourse(): CourseEntity?

    fun saveCourses(courses: List<CourseEntity>)
    fun getCourses(): List<CourseEntity>

    suspend fun setGuestMode()
    suspend fun isGuest(): Boolean
    suspend fun resetGuestMode()

    suspend fun saveEmail(email: String)
    suspend fun getEmail(): String?

    suspend fun isOfflineMode(): Boolean
    suspend fun setOfflineMode(set: Boolean)
}