package usecase.auth

import data.prefs.IDataStoreManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import presentation.usecases.auth.ISetGuestUseCase

class SetGuestUseCase(private val dataStore: IDataStoreManager) : ISetGuestUseCase {

    override suspend fun setGuest() {
        dataStore.setGuestMode()
    }

    override suspend fun resetGuest() {
        dataStore.resetGuestMode()
    }

    override suspend fun isGuestMode() = dataStore.isGuest()

    override fun isGuestModeFlow(): Flow<Boolean> = flow {
        emit(dataStore.isGuest())
    }
}