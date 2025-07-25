package usecase.auth

import data.prefs.IDataStoreManager
import presentation.usecases.auth.ISetGuestUseCase

class SetGuestUseCase(private val dataStore: IDataStoreManager) : ISetGuestUseCase {

    override suspend fun setGuest() {
        dataStore.setGuestMode()
    }

    override suspend fun resetGuest() {
        dataStore.resetGuestMode()
    }

    override suspend fun isGuestMode() = dataStore.isGuest()
}