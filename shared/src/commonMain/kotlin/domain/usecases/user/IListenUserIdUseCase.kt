package domain.usecases.user

import kotlinx.coroutines.flow.Flow

interface IListenUserIdUseCase {

    fun invoke(): Flow<String?>

}