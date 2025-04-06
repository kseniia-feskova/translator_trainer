package translator.data.repository.words

import translator.data.model.base.Result
import translator.data.model.words.WordResponse
import translator.data.model.words.add.AddWordRequest
import translator.data.model.words.get.bytranslate.WordByOriginalRequest
import translator.data.model.words.get.bytranslate.WordByTranslatedRequest
import translator.data.model.words.update.UpdateWordStatusRequest
import java.util.UUID

interface IWordRepository {

    suspend fun addWord(request: AddWordRequest): Result<WordResponse>

    suspend fun getWordByTranslated(request: WordByTranslatedRequest): Result<WordResponse>

    suspend fun getWordByOriginal(request: WordByOriginalRequest): Result<WordResponse>

    suspend fun getAllWords(): Result<List<WordResponse>>

    suspend fun getWordsBySet(setId: UUID): Result<List<WordResponse>>

    suspend fun updateStatus(
        wordId: UUID,
        updateStatus: UpdateWordStatusRequest
    ): Result<WordResponse>

    suspend fun delete(wordId: UUID): Result<Void>

}