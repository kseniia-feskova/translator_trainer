package data.repos

import data.api.ApiService
import data.safeCall
import data.repository.ISetRepository
import data.model.sets.AddSetRequest
import data.model.sets.SetResponse
import data.model.base.Result

class SetApiRepository(private val apiService: ApiService) : ISetRepository {

    override suspend fun addSet(request: AddSetRequest): Result<SetResponse> {
        return safeCall(request = { apiService.saveSet(request) })
    }

    override suspend fun getAllSets(courseId: String): Result<List<SetResponse>> {
        return safeCall(request = {
            apiService.getAllSets(
                data.model.sets.get.all.GetAllRequest(courseId)
            )
        })
    }
}