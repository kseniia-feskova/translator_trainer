package com.presentation.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

val Context.dataStore by preferencesDataStore(name = "app_preferences")

interface IDataStoreManager {
    fun listenSelectedSetId(): Flow<Int?>
    fun listenUserId(): Flow<UUID?>
    suspend fun saveSelectedSetId(id: Int)
    suspend fun clearSelectedSetId()
    suspend fun saveUserId(id: UUID?)
}

class DataStoreManager(private val context: Context) : IDataStoreManager {

    private val SELECTED_SET_ID = intPreferencesKey("selected_set_id")
    private val USER_ID = stringPreferencesKey("user_id")

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
        context.dataStore.edit { preferences ->
            preferences[USER_ID] = id.toString()
        }
    }
}