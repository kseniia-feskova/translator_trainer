package data.prefs

interface ITokenStorage {

    fun saveToken(key: String, token: String)

    fun getToken(key: String): String?

    fun clearToken(key: String)

    companion object {
        const val ACCESS_TOKEN = "access-token"
    }
}
