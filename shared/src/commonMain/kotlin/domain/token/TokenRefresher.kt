package domain.token

import data.prefs.IDataStoreManager
import data.prefs.ILocalDatabase
import data.prefs.ITokenStorage
import data.repository.auth.IAuthRepository
import domain.cache.ISetsCacheProvider

class TokenRefresher(
    private val authRepo: IAuthRepository,
    private val tokenStorage: ITokenStorage,
    private val cache: ISetsCacheProvider,
    private val prefs: IDataStoreManager,
    private val localDatabase: ILocalDatabase
) : ITokenRefresher {

    override suspend fun refreshToken(): Boolean {
        val refreshToken = tokenStorage.getToken(REFRESH_TOKEN)
        if (refreshToken != null) {
            tokenStorage.clearToken(ACCESS_TOKEN)
            val response = authRepo.refreshToken(refreshToken)
            val body = response.data
            if (body != null) {
                println("refreshToken: Token updated")
                tokenStorage.saveToken(ACCESS_TOKEN, body.accessToken)
                tokenStorage.saveToken(REFRESH_TOKEN, body.refreshToken)
                return true
            } else {
                if (response.errorMsg == ERROR_TOKEN_EXPIRED) {
                    println("refreshToken: Code = 401")
                    authRepo.logout()
                    localDatabase.clearDatabase()
                    cache.clear()
                    prefs.saveUserId(null)
                } else {
                    println("TokenRefresher: Can not refresh token. Error msg = ${response.errorMsg}")
                }
                return false
            }
        } else {
            println("refreshToken: Token is null(")
            authRepo.logout()
            localDatabase.clearDatabase()
            cache.updateSets(null)
            prefs.saveUserId(null)
            return false
        }
    }

    companion object {
        const val ACCESS_TOKEN = "accessToken"
        const val REFRESH_TOKEN = "refreshToken"
        const val ERROR_TOKEN_EXPIRED = "Need to refresh token"
    }
}