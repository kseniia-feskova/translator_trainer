package com.data.repository.words.api

import com.data.api.ApiService
import com.data.model.base.Result
import com.data.model.words.WordResponse
import com.data.model.words.add.AddWordRequest
import com.data.model.words.get.bytranslate.WordByOriginalRequest
import com.data.model.words.get.bytranslate.WordByTranslatedRequest
import com.data.model.words.update.UpdateWordStatusRequest
import com.data.safeCall
import java.util.UUID

class WordsApiRepository(
    private val apiService: ApiService
) : IWordsApiRepository {

    override suspend fun addWord(request: AddWordRequest): Result<WordResponse> {
        return safeCall(request = { apiService.saveWorld(request) })
    }

    override suspend fun getWordByTranslated(request: WordByTranslatedRequest): Result<WordResponse> {
        return safeCall(request = { apiService.getWordByTranslated(request) })
    }

    override suspend fun getWordByOriginal(request: WordByOriginalRequest): Result<WordResponse> {
        return safeCall(request = { apiService.getWordByOriginal(request) })
    }

    override suspend fun getWordsBySet(setId: UUID): Result<List<WordResponse>> {
        return safeCall(request = { apiService.getWordsBySet(setId) })
    }

    override suspend fun updateStatus(
        wordId: UUID,
        updateStatus: UpdateWordStatusRequest
    ): Result<WordResponse> {
        return safeCall(request = { apiService.updateStatus(wordId, updateStatus) })
    }

}