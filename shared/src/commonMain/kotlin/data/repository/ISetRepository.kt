package data.repository

import data.model.base.Result
import data.model.sets.AddSetRequest
import data.model.sets.SetResponse

interface ISetRepository {

    suspend fun addSet(request: AddSetRequest): Result<SetResponse>

    suspend fun getAllSets(courseId: String): Result<List<SetResponse>>
}