package data.repository.set

import data.api.ApiService
import data.model.base.Result
import data.model.sets.AddSetRequest
import data.model.sets.SetResponse
import data.repository.safeCall

class SetApiRepository(private val apiService: ApiService) : ISetApiRepository {

    override suspend fun addSet(request: AddSetRequest): Result<SetResponse> {
        return safeCall(request = { apiService.saveSet(request) })
    }

    override suspend fun getAllSets(courseId: String): Result<List<SetResponse>> {
        return safeCall(request = {
            apiService.getAllSets(courseId)
        })
    }
}