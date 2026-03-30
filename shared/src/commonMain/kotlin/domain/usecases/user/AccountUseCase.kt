package domain.usecases.user

import domain.usecases.IAccountUseCase
import data.prefs.IDataStoreManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AccountUseCase(
    private val prefs: IDataStoreManager
) : IAccountUseCase {

    override suspend fun getUserId(): String? {
        return prefs.getUserId()
    }

    override fun getUserIdFlow(): Flow<String?> = flow {
        emit(prefs.getUserId())
    }

}