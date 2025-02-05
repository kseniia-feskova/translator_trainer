package com.data.repository.words.api

import com.data.api.Result
import com.data.model.words.AddWordRequest
import com.data.model.words.WordResponse
import com.data.model.words.get.bytranslate.WordByOriginalRequest
import com.data.model.words.get.bytranslate.WordByTranslatedRequest

interface IWordsApiRepository {

    suspend fun addWord(request: AddWordRequest): Result<WordResponse>

    suspend fun getWordByTranslated(request: WordByTranslatedRequest): Result<WordResponse>

    suspend fun getWordByOriginal(request: WordByOriginalRequest): Result<WordResponse>

}