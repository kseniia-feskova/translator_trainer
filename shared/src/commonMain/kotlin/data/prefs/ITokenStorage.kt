package data.prefs

interface ITokenStorage {

    fun saveToken(key: String, token: String)

    fun getToken(key: String): String?

    fun clearToken(key: String)
}
