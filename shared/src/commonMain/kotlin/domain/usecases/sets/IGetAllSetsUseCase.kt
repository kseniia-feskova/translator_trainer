package domain.usecases.sets

import data.model.sets.SetResponse
import kotlinx.coroutines.flow.Flow

interface IGetAllSetsUseCase {

    fun invokeFlow():Flow<List<SetResponse>>
    suspend fun invoke(): Result<List<SetResponse>>

}