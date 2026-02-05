package presentation.usecases.words

import kotlinx.coroutines.flow.Flow
import presentation.model.WordUI

interface IGetWordsBySetUseCase {
    suspend fun invoke(setId: String): Result<List<WordUI>>
    fun invokeFlow(setId: String): Flow<Result<List<WordUI>>>
}