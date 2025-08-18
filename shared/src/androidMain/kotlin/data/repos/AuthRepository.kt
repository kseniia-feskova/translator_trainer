package data.repos

import data.room.AppDao
import data.api.ApiService
import data.prefs.ITokenStorage
import data.repository.IAuthRepository
import data.safeCall
import domain.token.TokenRefresher.Companion.ACCESS_TOKEN
import domain.token.TokenRefresher.Companion.REFRESH_TOKEN

class AuthRepository(
    private val service: ApiService,
    private val tokenStorage: ITokenStorage,
    private val appDao: AppDao
) : IAuthRepository {

    override suspend fun register(
        email: String,
        username: String,
        password: String
    ): data.model.base.Result<data.model.auth.AuthResponse> {
        val request = data.model.auth.AuthRequest(
            email = email,
            phone = email,
            username = username,
            password = password
        )
        return safeCall(
            request = { service.register(request) },
            onSuccess = { body ->
                tokenStorage.saveToken(ACCESS_TOKEN, body.accessToken)
                tokenStorage.saveToken(REFRESH_TOKEN, body.refreshToken)
                if (body.uuid == null) {
                    data.model.base.Result(errorMsg = "Empty uuid")
                } else {
                    data.model.base.Result(data = body)
                }
            })
    }

    override suspend fun login(
        email: String,
        username: String,
        password: String
    ): data.model.base.Result<data.model.auth.AuthResponse> {
        val request =
            data.model.auth.AuthRequest(email = email, username = username, password = password)
        return safeCall({
            service.login(request)
        }, onSuccess = { body ->
            tokenStorage.saveToken(ACCESS_TOKEN, body.accessToken)
            tokenStorage.saveToken(REFRESH_TOKEN, body.refreshToken)
            if (body.uuid == null) {
                data.model.base.Result(errorMsg = "Empty user id")
            } else {
                data.model.base.Result(data = body)
            }
        })
    }

    override suspend fun refreshToken(token: String): data.model.base.Result<data.model.auth.AuthResponse> {
        return safeCall({ service.refreshToken(data.model.auth.RefreshTokenRequest(token)) })
    }


    override suspend fun verify(email: String, code: String): data.model.base.Result<data.model.auth.AuthResponse> {
        return safeCall({
            service.verifyCode(email, code)
        }, onSuccess = { body ->
            tokenStorage.saveToken(ACCESS_TOKEN, body.accessToken)
            tokenStorage.saveToken(REFRESH_TOKEN, body.refreshToken)
            if (body.uuid == null) {
                data.model.base.Result(errorMsg = "Empty user id")
            } else {
                data.model.base.Result(data = body)
            }
        })
    }

    override suspend fun resendCode(email: String): data.model.base.Result<data.model.auth.AuthResponse> {
        return safeCall({
            service.resendCode(email)
        }, onSuccess = { body ->
            if (body.uuid == null) {
                data.model.base.Result(errorMsg = "Empty user id")
            } else {
                data.model.base.Result(data = body)
            }
        })
    }

    override suspend fun clearCode(email: String) {
        safeCall({ service.clearCode(email) }, onSuccess = {})
    }

    override suspend fun logout() {
        tokenStorage.clearToken(ACCESS_TOKEN)
        tokenStorage.clearToken(REFRESH_TOKEN)
        clearDatabase()
    }

    private suspend fun clearDatabase() {
        appDao.clearUsers()
        appDao.clearSets()
        appDao.clearWords()
    }
}
