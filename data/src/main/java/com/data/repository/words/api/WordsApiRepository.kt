package com.data.repository.words.api

import com.data.api.ApiService
import com.data.api.Result
import com.data.model.words.AddWordRequest
import com.data.model.words.WordResponse
import com.data.model.words.get.bytranslate.WordByTranslatedRequest
import com.data.safeCall

class WordsApiRepository(
    private val apiService: ApiService
) : IWordsApiRepository {

    override suspend fun addWord(request: AddWordRequest): Result<WordResponse> {
        return safeCall(request = { apiService.saveWorld(request) })
    }

    override suspend fun getWordByTranslated(request: WordByTranslatedRequest): Result<WordResponse> {
        return safeCall(request = { apiService.getWordByTranslated(request) })
    }

}