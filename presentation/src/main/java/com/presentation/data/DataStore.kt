package com.presentation.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.presentation.utils.Language
import com.presentation.utils.getLanguageByCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.util.UUID

val Context.dataStore by preferencesDataStore(name = "app_preferences")

interface IDataStoreManager {
    fun listenSelectedSetId(): Flow<Int?>
    fun listenUserId(): Flow<UUID?>
    suspend fun saveSelectedSetId(id: Int)
    suspend fun clearSelectedSetId()
    suspend fun saveUserId(id: UUID?)

    suspend fun setOriginalLanguage(language: Language)
    suspend fun setResultLanguage(language: Language)
    suspend fun getOriginalLanguage(): Language?
    suspend fun getResultLanguage(): Language?
}

class DataStoreManager(private val context: Context) : IDataStoreManager {

    private val SELECTED_SET_ID = intPreferencesKey("selected_set_id")
    private val USER_ID = stringPreferencesKey("user_id")
    private val ORIGINAL_LANGUAGE = stringPreferencesKey("original_language")
    private val RESULT_LANGUAGE = stringPreferencesKey("result_language")

    private val selectedSetId: Flow<Int?> = context.dataStore.data
        .map { preferences ->
            preferences[SELECTED_SET_ID]
        }

    private val userId: Flow<UUID?> = context.dataStore.data
        .map { preferences ->
            if (preferences[USER_ID].isNullOrEmpty()) {
                null
            } else {
                UUID.fromString(preferences[USER_ID])
            }
        }

    override fun listenUserId(): Flow<UUID?> = userId

    override fun listenSelectedSetId() = selectedSetId

    override suspend fun saveSelectedSetId(id: Int) {
        context.dataStore.edit { preferences ->
            preferences[SELECTED_SET_ID] = id
        }
    }

    override suspend fun clearSelectedSetId() {
        context.dataStore.edit { preferences ->
            preferences.remove(SELECTED_SET_ID)
        }
    }

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

    override suspend fun setOriginalLanguage(language: Language) {
        context.dataStore.edit { preferences ->
            preferences[ORIGINAL_LANGUAGE] = language.code
        }
    }

    override suspend fun setResultLanguage(language: Language) {
        context.dataStore.edit { preferences ->
            preferences[RESULT_LANGUAGE] = language.code
        }
    }

    override suspend fun getOriginalLanguage(): Language? {
        return context.dataStore.data.map { preferences ->
            preferences[ORIGINAL_LANGUAGE]
        }.firstOrNull().getLanguageByCode()
    }

    override suspend fun getResultLanguage(): Language? {
        return context.dataStore.data.map { preferences ->
            preferences[RESULT_LANGUAGE]
        }.firstOrNull().getLanguageByCode()
    }
}