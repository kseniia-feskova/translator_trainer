package usecase

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import data.prefs.dataStore
import domain.usecases.user.IListenUserIdUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ListenUserIdUseCase(context: Context) : IListenUserIdUseCase {
    private val userIdKey = stringPreferencesKey("user_id")

    private val userId: Flow<String?> = context.dataStore.data
        .map { preferences ->
            if (preferences[userIdKey].isNullOrEmpty()) {
                null
            } else {
                preferences[userIdKey]
            }
        }

    override fun invoke(): Flow<String?> = userId
}