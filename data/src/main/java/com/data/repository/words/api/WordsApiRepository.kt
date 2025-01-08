package com.data.repository.words.api

import com.data.api.ApiService
import com.data.api.Result
import com.data.model.words.AddWordRequest
import com.data.model.words.AddWordResponse
import com.data.repository.user.saveCall

class WordsApiRepository(
    private val apiService: ApiService
) : IWordsApiRepository {

    override suspend fun addWord(request: AddWordRequest): Result<AddWordResponse> {
        return saveCall(request = { apiService.saveWorld(request) })
    }

}