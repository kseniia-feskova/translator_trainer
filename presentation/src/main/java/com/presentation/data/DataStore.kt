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

    private val USER_ID = stringPreferencesKey("user_id")
    private val COURSE_KEY = stringPreferencesKey("course")

    private val userId: Flow<UUID?> = context.dataStore.data
        .map { preferences ->
            if (preferences[USER_ID].isNullOrEmpty()) {
                null
            } else {
                UUID.fromString(preferences[USER_ID])
            }
        }

    private val coursesList = mutableListOf<CourseUI>()

    override fun listenUserId(): Flow<UUID?> = userId

    override suspend fun saveUserId(id: UUID?) {
        if (id == null) {
            context.dataStore.edit { preferences ->
                preferences.remove(USER_ID)
            }
        } else {
            context.dataStore.edit { preferences ->
                preferences[USER_ID] = id.toString()
            }
        }
    }

    override fun saveCourses(courses: List<CourseUI>) {
        coursesList.clear()
        coursesList.addAll(courses)
    }

    override fun getCourses(): List<CourseUI> = coursesList.toList()

    override suspend fun saveCourse(course: CourseUI?) {
        context.dataStore.edit { preferences ->
            preferences[COURSE_KEY] = Json.encodeToString(course)
        }
    }

    override suspend fun getCourse(): CourseUI? {
        return context.dataStore.data.map { preferences ->
            preferences[COURSE_KEY]?.let { Json.decodeFromString<CourseUI>(it) }
        }.firstOrNull()
    }
}
