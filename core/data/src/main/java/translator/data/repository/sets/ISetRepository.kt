package translator.data.repository.sets

import translator.data.model.base.Result
import translator.data.model.sets.AddSetRequest
import translator.data.model.sets.SetResponse
import java.util.UUID

interface ISetRepository {

    suspend fun addSet(request: AddSetRequest): Result<SetResponse>

    suspend fun getAllSets(courseId: UUID): Result<List<SetResponse>>
}