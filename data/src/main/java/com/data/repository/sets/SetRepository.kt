package com.data.repository.sets

import com.data.api.ApiService
import com.data.api.Result
import com.data.model.sets.AddSetRequest
import com.data.model.sets.SetResponse
import com.data.model.sets.get.all.GetAllRequest
import com.data.safeCall
import java.util.UUID

class SetRepository(private val apiService: ApiService) : ISetRepository {

    override suspend fun addSet(request: AddSetRequest): Result<SetResponse> {
        return safeCall(request = { apiService.saveSet(request) })
    }

    override suspend fun getAllSets(courseId: UUID): Result<List<SetResponse>> {
        return safeCall(request = { apiService.getAllSets(GetAllRequest(courseId)) })
    }

}