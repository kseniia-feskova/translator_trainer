package com.data.repository.words.api

import com.data.model.base.Result
import com.data.model.words.WordResponse
import com.data.model.words.add.AddWordRequest
import com.data.model.words.get.bytranslate.WordByOriginalRequest
import com.data.model.words.get.bytranslate.WordByTranslatedRequest
import com.data.model.words.update.UpdateWordStatusRequest
import java.util.UUID

interface IWordsApiRepository {

    suspend fun addWord(request: AddWordRequest): Result<WordResponse>

    suspend fun getWordByTranslated(request: WordByTranslatedRequest): Result<WordResponse>

    suspend fun getWordByOriginal(request: WordByOriginalRequest): Result<WordResponse>

    suspend fun getWordsBySet(setId: UUID): Result<List<WordResponse>>

    suspend fun updateStatus(
        wordId: UUID,
        updateStatus: UpdateWordStatusRequest
    ): Result<WordResponse>

}