package usecase.user

import presentation.usecases.IAccountUseCase
import data.prefs.IDataStoreManager

class AccountUseCase(
    private val prefs: IDataStoreManager
) : IAccountUseCase {

    override suspend fun getUserId(): String? {
        return prefs.getUserId()
    }

}