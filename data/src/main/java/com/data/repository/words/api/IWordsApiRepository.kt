package com.data.repository.words.api

import com.data.api.Result
import com.data.model.words.AddWordRequest
import com.data.model.words.AddWordResponse

interface IWordsApiRepository {

    suspend fun addWord(request: AddWordRequest): Result<AddWordResponse>
}