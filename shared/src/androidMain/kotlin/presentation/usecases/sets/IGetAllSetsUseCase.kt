package presentation.usecases.sets

import kotlinx.coroutines.flow.Flow
import presentation.model.SetOfCards

interface IGetAllSetsUseCase {

    fun invokeFlow(): Flow<List<SetOfCards>>
    suspend fun invoke(): Result<List<SetOfCards>>

}