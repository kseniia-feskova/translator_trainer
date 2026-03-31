package data.repository.word

import data.model.base.Result
import data.model.words.WordResponse
import data.model.words.add.AddWordRequest
import data.model.words.get.bytranslate.WordByOriginalRequest
import data.model.words.get.bytranslate.WordByTranslatedRequest
import data.model.words.update.UpdateWordStatusRequest

interface IWordApiRepository {

    suspend fun addWord(request: AddWordRequest): Result<WordResponse>

    suspend fun getWordByTranslated(request: WordByTranslatedRequest): Result<WordResponse>

    suspend fun getWordByOriginal(request: WordByOriginalRequest): Result<WordResponse>

    suspend fun getAllWords(): Result<List<WordResponse>>

    suspend fun getWordsBySet(setId: String): Result<List<WordResponse>>

    suspend fun updateStatus(
        wordId: String,
        updateStatus: UpdateWordStatusRequest
    ): Result<WordResponse>

    suspend fun delete(wordId: String): Result<Unit>

}