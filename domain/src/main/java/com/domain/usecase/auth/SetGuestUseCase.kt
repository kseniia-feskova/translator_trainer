package com.domain.usecase.auth

import translator.data.prefs.IDataStoreManager
import com.presentation.usecases.auth.ISetGuestUseCase

class SetGuestUseCase(private val dataStore: IDataStoreManager) : ISetGuestUseCase {

    override suspend fun setGuest(){
        dataStore.setGuestMode()
    }

    override suspend fun resetGuest(){
        dataStore.resetGuestMode()
    }

    override suspend fun isGuestMode() = dataStore.isGuest()
}