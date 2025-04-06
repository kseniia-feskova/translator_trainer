package com.domain.usecase.user

import translator.data.prefs.IDataStoreManager
import com.presentation.usecases.IAccountUseCase
import java.util.UUID

class AccountUseCase(
    private val prefs: IDataStoreManager
) : IAccountUseCase {

    override suspend fun getUserId(): UUID? {
        return prefs.getUserId()
    }

}