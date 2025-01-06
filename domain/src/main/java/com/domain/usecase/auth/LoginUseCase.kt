package com.domain.usecase.auth

import android.util.Log
import com.data.repository.auth.IAuthRepository
import com.presentation.usecases.auth.ILoginUseCase
import java.util.UUID

class LoginUseCase(private val repo: IAuthRepository) : ILoginUseCase {

    override suspend fun invoke(email: String, password: String): Result<UUID?> {
        val response = repo.login(email, password)
        Log.e("LoginUseCase", "response = ${response.errorMsg}")
        return if (response.errorMsg.isNotEmpty()) {
            Log.e("LoginUseCase", "Exception = ${Exception(response.errorMsg)}")
            Result.failure(Exception(response.errorMsg))
        } else Result.success(response.data?.uuid)
    }

}