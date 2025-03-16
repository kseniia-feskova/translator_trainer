package com.data.prefs

import com.data.model.course.CourseEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface IDataStoreManager {
    fun listenUserId(): Flow<UUID?>

    suspend fun saveUserId(id: UUID?)
    suspend fun getUserId(): UUID?

    suspend fun saveCourse(course: CourseEntity?)
    suspend fun getCourse(): CourseEntity?

    fun saveCourses(courses: List<CourseEntity>)
    fun getCourses(): List<CourseEntity>

    suspend fun setGuestMode()
    suspend fun isGuest(): Boolean
    fun isGuestOnRuntime(): Boolean?
    suspend fun resetGuestMode()
}