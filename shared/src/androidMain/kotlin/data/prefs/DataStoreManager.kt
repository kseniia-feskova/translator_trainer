package data.prefs

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val Context.dataStore by preferencesDataStore(name = "app_preferences")

class DataStoreManager(private val context: Context) : IDataStoreManager {

    private val userIdKey = stringPreferencesKey("user_id")
    private val courseKey = stringPreferencesKey("course")
    private val emailKey = stringPreferencesKey("email")
    private val isGuestKey = booleanPreferencesKey("is_guest")
    private var isGuest: Boolean? = null

    private val userId: Flow<String?> = context.dataStore.data
        .map { preferences ->
            if (preferences[userIdKey].isNullOrEmpty()) {
                null
            } else {
                preferences[userIdKey]
            }
        }

    private val coursesList = mutableListOf<data.model.course.CourseEntity>()

    override fun listenUserId(): Flow<String?> = userId

    override suspend fun saveUserId(id: String?) {
        context.dataStore.edit { preferences ->
            preferences.remove(emailKey)
        }
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

    override suspend fun getUserId(): String? {
        return context.dataStore.data.map {
            if (it[userIdKey] == null) null
            else {
                it[userIdKey]
            }
        }.firstOrNull()
    }

    override fun saveCourses(courses: List<data.model.course.CourseEntity>) {
        coursesList.clear()
        coursesList.addAll(courses)
    }

    override fun getCourses(): List<data.model.course.CourseEntity> = coursesList.toList()

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

    override suspend fun saveEmail(email: String) {
        context.dataStore.edit { preferences ->
            preferences[emailKey] = email
        }
    }

    override suspend fun getEmail(): String? {
        return context.dataStore.data.map { preferences ->
            preferences[emailKey]
        }.firstOrNull()
    }


    override suspend fun saveCourse(course: data.model.course.CourseEntity?) {
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

    override suspend fun getCourse(): data.model.course.CourseEntity? {
        return context.dataStore.data.map { preferences ->
            preferences[courseKey]?.let { Json.decodeFromString<data.model.course.CourseEntity>(it) }
        }.firstOrNull()
    }
}