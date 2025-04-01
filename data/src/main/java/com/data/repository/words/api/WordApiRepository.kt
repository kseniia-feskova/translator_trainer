package com.data.repository.words.api

import com.data.api.ApiService
import com.data.model.base.Result
import com.data.model.words.WordResponse
import com.data.model.words.add.AddWordRequest
import com.data.model.words.get.bytranslate.WordByOriginalRequest
import com.data.model.words.get.bytranslate.WordByTranslatedRequest
import com.data.model.words.update.UpdateWordStatusRequest
import com.data.repository.words.IWordRepository
import com.data.safeCall
import java.util.UUID

class WordApiRepository(
    private val apiService: ApiService
) : IWordRepository, IWordApiRepository {

    override suspend fun addWord(request: AddWordRequest): Result<WordResponse> {
        return safeCall(request = { apiService.saveWorld(request) })
    }

    override suspend fun getWordByTranslated(request: WordByTranslatedRequest): Result<WordResponse> {
        return safeCall(request = { apiService.getWordByTranslated(request) })
    }

    override suspend fun getWordByOriginal(request: WordByOriginalRequest): Result<WordResponse> {
        return safeCall(request = { apiService.getWordByOriginal(request) })
    }

    override suspend fun getAllWords(): Result<List<WordResponse>> {
        return Result(data = emptyList())
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

    override suspend fun delete(wordId: UUID): Result<Void> {
        return safeCall(request = { apiService.deleteWord(wordId) })
    }

}