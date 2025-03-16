package com.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.data.model.course.CourseEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.UUID

val Context.dataStore by preferencesDataStore(name = "app_preferences")

class DataStoreManager(private val context: Context) : IDataStoreManager {

    private val userIdKey = stringPreferencesKey("user_id")
    private val courseKey = stringPreferencesKey("course")
    private val isGuestKey = booleanPreferencesKey("is_guest")
    private var isGuest: Boolean? = null

    private val userId: Flow<UUID?> = context.dataStore.data
        .map { preferences ->
            if (preferences[userIdKey].isNullOrEmpty()) {
                null
            } else {
                UUID.fromString(preferences[userIdKey])
            }
        }

    private val coursesList = mutableListOf<CourseEntity>()

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

    override suspend fun getUserId(): UUID? {
        return context.dataStore.data.map {
            if (it[userIdKey] == null) null
            else {
                UUID.fromString(it[userIdKey])
            }
        }.firstOrNull()
    }

    override fun saveCourses(courses: List<CourseEntity>) {
        coursesList.clear()
        coursesList.addAll(courses)
    }

    override fun getCourses(): List<CourseEntity> = coursesList.toList()

    override suspend fun setGuestMode() {
        isGuest = true
        context.dataStore.edit { preferences ->
            preferences[isGuestKey] = true
        }
    }

    override suspend fun isGuest(): Boolean {
        isGuest = context.dataStore.data.map { prefs -> prefs[isGuestKey] }.firstOrNull() ?: false
        return isGuest == true
    }

    override fun isGuestOnRuntime(): Boolean? {
        return isGuest
    }

    override suspend fun resetGuestMode() {
        this.isGuest = false
        context.dataStore.edit { preferences ->
            preferences[isGuestKey] = false
        }
    }

    override suspend fun saveCourse(course: CourseEntity?) {
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

    override suspend fun getCourse(): CourseEntity? {
        return context.dataStore.data.map { preferences ->
            preferences[courseKey]?.let { Json.decodeFromString<CourseEntity>(it) }
        }.firstOrNull()
    }
}