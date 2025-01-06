package com.data.prefs

import android.content.SharedPreferences
import java.util.UUID

interface ITokenStorage {

    fun saveToken(key: String, token: String)

    fun getToken(key: String): String?

    fun saveUserId(id: UUID)

    fun getUserId(): UUID
}


class TokenStorage(private val sharedPreferences: SharedPreferences) : ITokenStorage {

    override fun saveToken(key: String, token: String) {
        sharedPreferences.edit().putString(key, token).apply()
    }

    override fun getToken(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    override fun saveUserId(id: UUID) {
        sharedPreferences.edit().putString(USER_ID, id.toString()).apply()
    }

    override fun getUserId(): UUID {
        return UUID.fromString(sharedPreferences.getString(USER_ID, ""))
    }

    companion object {
        const val ACCESS_TOKEN = "accessToken"
        const val REFRESH_TOKEN = "refreshToken"
        private const val USER_ID = "userId"
    }
}