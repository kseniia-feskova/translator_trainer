package com.data.repository.sets

import com.data.api.Result
import com.data.model.sets.AddSetRequest
import com.data.model.sets.SetResponse
import java.util.UUID

interface ISetRepository {

    suspend fun addSet(request: AddSetRequest): Result<SetResponse>

    suspend fun getAllSets(courseId: UUID): Result<List<SetResponse>>
}