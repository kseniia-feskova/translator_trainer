package data.repository.auth

import data.api.ApiService
import data.model.auth.AuthRequest
import data.model.auth.AuthResponse
import data.model.auth.FirebaseAuthRequest
import data.model.auth.RefreshTokenRequest
import data.model.base.Result
import data.prefs.ITokenStorage
import data.repository.safeCall
import domain.token.TokenRefresher

class AuthRepository(
    private val service: ApiService,
    private val tokenStorage: ITokenStorage
) : IAuthRepository {

    override suspend fun register(
        email: String,
        username: String,
        password: String
    ): Result<AuthResponse> {
        val request = AuthRequest(
            email = email,
            phone = email,
            username = username,
            password = password
        )
        return safeCall(
            request = { service.register(request) },
            onSuccess = { body ->
                tokenStorage.saveToken(TokenRefresher.ACCESS_TOKEN, body.accessToken)
                tokenStorage.saveToken(TokenRefresher.REFRESH_TOKEN, body.refreshToken)
                if (body.uuid == null) {
                    Result(errorMsg = "Empty uuid")
                } else {
                    Result(data = body)
                }
            })
    }

    override suspend fun login(
        email: String,
        username: String,
        password: String
    ): Result<AuthResponse> {
        val request =
            AuthRequest(
                email = email,
                username = username,
                password = password,
                phone = email
            )
        return safeCall({
            service.login(request)
        }, onSuccess = { body ->
            tokenStorage.saveToken(TokenRefresher.ACCESS_TOKEN, body.accessToken)
            tokenStorage.saveToken(TokenRefresher.REFRESH_TOKEN, body.refreshToken)
            if (body.uuid == null) {
                Result(errorMsg = "Empty user id")
            } else {
                Result(data = body)
            }
        })
    }

    override suspend fun refreshToken(token: String): Result<AuthResponse> {
        return safeCall({ service.refreshToken(RefreshTokenRequest(token)) })
    }


    override suspend fun verify(email: String, code: String): Result<AuthResponse> {
        return safeCall({
            service.verifyCode(email, code)
        }, onSuccess = { body ->
            tokenStorage.saveToken(TokenRefresher.ACCESS_TOKEN, body.accessToken)
            tokenStorage.saveToken(TokenRefresher.REFRESH_TOKEN, body.refreshToken)
            if (body.uuid == null) {
                Result(errorMsg = "Empty user id")
            } else {
                Result(data = body)
            }
        })
    }

    override suspend fun resendCode(email: String): Result<AuthResponse> {
        return safeCall({
            service.resendCode(email)
        }, onSuccess = { body ->
            if (body.uuid == null) {
                Result(errorMsg = "Empty user id")
            } else {
                Result(data = body)
            }
        })
    }

    override suspend fun clearCode(email: String) {
        safeCall<Unit>({ service.clearCode(email) }, onSuccess = {})
    }

    override suspend fun logout() {
        tokenStorage.clearToken(TokenRefresher.ACCESS_TOKEN)
        tokenStorage.clearToken(TokenRefresher.REFRESH_TOKEN)
    }

    override suspend fun registerWithFirebase(
        request: FirebaseAuthRequest
    ): Result<AuthResponse> {
        return safeCall(
            request = { service.loginWithFirebase(request) },
            onSuccess = { body ->
                tokenStorage.saveToken(TokenRefresher.ACCESS_TOKEN, body.accessToken)
                tokenStorage.saveToken(TokenRefresher.REFRESH_TOKEN, body.refreshToken)
                if (body.uuid == null) {
                    Result(errorMsg = "Empty uuid")
                } else {
                    Result(data = body)
                }
            }
        )
    }
}