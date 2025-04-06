package translator.data.repository.sets.api

import translator.data.api.ApiService
import translator.data.model.base.Result
import translator.data.model.sets.AddSetRequest
import translator.data.model.sets.SetResponse
import translator.data.model.sets.get.all.GetAllRequest
import translator.data.repository.sets.ISetRepository
import translator.data.safeCall
import java.util.UUID

class SetApiRepository(private val apiService: ApiService) : ISetRepository {

    override suspend fun addSet(request: AddSetRequest): Result<SetResponse> {
        return safeCall(request = { apiService.saveSet(request) })
    }

    override suspend fun getAllSets(courseId: UUID): Result<List<SetResponse>> {
        return safeCall(request = { apiService.getAllSets(GetAllRequest(courseId)) })
    }
}