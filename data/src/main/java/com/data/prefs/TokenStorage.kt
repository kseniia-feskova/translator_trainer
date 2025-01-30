package com.data.prefs

import android.content.SharedPreferences

interface ITokenStorage {

    fun saveToken(key: String, token: String)

    fun getToken(key: String): String?

    fun clearToken(key: String)

}


class TokenStorage(private val sharedPreferences: SharedPreferences) : ITokenStorage {

    override fun saveToken(key: String, token: String) {
        sharedPreferences.edit().putString(key, token).apply()
    }

    override fun getToken(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    override fun clearToken(key: String) {
        sharedPreferences.edit().putString(key, null).apply()
    }

    companion object {
        const val ACCESS_TOKEN = "accessToken"
        const val REFRESH_TOKEN = "refreshToken"
    }
}