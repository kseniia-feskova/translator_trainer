package data.prefs

// encrypted local storage
interface ITokenStorage {

    fun saveToken(key: String, token: String)

    fun getToken(key: String): String?

    fun clearToken(key: String)
}
