package data.repository.set

import data.model.base.Result
import data.model.sets.AddSetRequest
import data.model.sets.SetResponse
import kotlinx.coroutines.flow.Flow

interface ISetDaoRepository {

    suspend fun addSet(request: AddSetRequest): Result<SetResponse>

    suspend fun getAllSets(courseId: String): Result<List<SetResponse>>

    suspend fun saveSets(sets: List<SetResponse>)

    suspend fun getAllSetsFlow(courseId: String): Flow<List<SetResponse>>

    suspend fun isSetsEmpty(courseId: String): Boolean
}