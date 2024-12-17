package com.presentation.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "app_preferences")

interface IDataStoreManager {
    fun listenSelectedSetId(): Flow<Int?>
    suspend fun saveSelectedSetId(id: Int)
    suspend fun clearSelectedSetId()
}

class DataStoreManager(private val context: Context) : IDataStoreManager {

    private val SELECTED_SET_ID = intPreferencesKey("selected_set_id")

    private val selectedSetId: Flow<Int?> = context.dataStore.data
        .map { preferences ->
            preferences[SELECTED_SET_ID]
        }

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
}