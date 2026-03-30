package domain.usecases.words

import data.model.words.WordResponse
import kotlinx.coroutines.flow.Flow

interface IGetWordsBySetUseCase {
    suspend fun invoke(setId: String): Result<List<WordResponse>>
    fun invokeFlow(setId: String): Flow<Result<List<WordResponse>>>
}