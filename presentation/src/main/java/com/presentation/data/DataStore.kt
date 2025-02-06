package com.presentation.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.presentation.model.CourseUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.UUID

val Context.dataStore by preferencesDataStore(name = "app_preferences")

interface IDataStoreManager {
    fun listenUserId(): Flow<UUID?>

    suspend fun saveUserId(id: UUID?)

    suspend fun saveCourse(course: CourseUI?)
    suspend fun getCourse(): CourseUI?

    fun saveCourses(courses: List<CourseUI>)
    fun getCourses(): List<CourseUI>
}

class DataStoreManager(private val context: Context) : IDataStoreManager {

    private val userIdKey = stringPreferencesKey("user_id")
    private val courseKey = stringPreferencesKey("course")

    private val userId: Flow<UUID?> = context.dataStore.data
        .map { preferences ->
            if (preferences[userIdKey].isNullOrEmpty()) {
                null
            } else {
                UUID.fromString(preferences[userIdKey])
            }
        }

    private val coursesList = mutableListOf<CourseUI>()

    override fun listenUserId(): Flow<UUID?> = userId

    override suspend fun saveUserId(id: UUID?) {
        if (id == null) {
            context.dataStore.edit { preferences ->
                preferences.remove(userIdKey)
            }
        } else {
            context.dataStore.edit { preferences ->
                preferences[userIdKey] = id.toString()
            }
        }
    }

    override fun saveCourses(courses: List<CourseUI>) {
        coursesList.clear()
        coursesList.addAll(courses)
    }

    override fun getCourses(): List<CourseUI> = coursesList.toList()

    override suspend fun saveCourse(course: CourseUI?) {
        if (course != null) {
            context.dataStore.edit { preferences ->
                preferences[courseKey] = Json.encodeToString(course)
            }
        } else {
            context.dataStore.edit { preferences ->
                preferences.remove(courseKey)
            }
        }
    }

    override suspend fun getCourse(): CourseUI? {
        return context.dataStore.data.map { preferences ->
            preferences[courseKey]?.let { Json.decodeFromString<CourseUI>(it) }
        }.firstOrNull()
    }
}
