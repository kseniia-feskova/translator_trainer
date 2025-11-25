package data.repos

import data.api.ApiService
import data.safeCall
import data.repository.set.ISetApiRepository
import data.model.sets.AddSetRequest
import data.model.sets.SetResponse
import data.model.base.Result
import java.util.UUID

class SetApiRepository(private val apiService: ApiService) : ISetApiRepository {

    override suspend fun addSet(request: AddSetRequest): Result<SetResponse> {
        return safeCall(request = { apiService.saveSet(request) })
    }

    override suspend fun getAllSets(courseId: String): Result<List<SetResponse>> {
        return safeCall(request = {
            apiService.getAllSets(UUID.fromString(courseId))
        })
    }
}