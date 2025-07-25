package data.prefs

import android.content.SharedPreferences
import androidx.core.content.edit

class TokenStorage(private val sharedPreferences: SharedPreferences) : ITokenStorage {

    override fun saveToken(key: String, token: String) {
        sharedPreferences.edit { putString(key, token) }
    }

    override fun getToken(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    override fun clearToken(key: String) {
        sharedPreferences.edit { putString(key, null) }
    }

}