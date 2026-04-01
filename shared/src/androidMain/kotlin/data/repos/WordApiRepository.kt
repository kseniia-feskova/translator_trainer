package data.repos

import data.api.ApiService
import data.model.base.Result
import data.model.words.WordResponse
import data.model.words.add.AddWordRequest
import data.model.words.get.bytranslate.WordByOriginalRequest
import data.model.words.get.bytranslate.WordByTranslatedRequest
import data.model.words.update.UpdateWordStatusRequest
import data.repository.word.IWordApiRepository
import data.safeCall

class WordApiRepository(
    private val apiService: ApiService
) : IWordApiRepository {

    override suspend fun addWord(request: AddWordRequest): Result<WordResponse> {
        return safeCall(request = { apiService.saveWorld(request) })
    }

    override suspend fun getWordByTranslated(request: WordByTranslatedRequest): Result<WordResponse> {
        return safeCall(request = {
            apiService.getWordByTranslated(
                request.courseId,
                request.translate
            )
        })
    }

    override suspend fun getWordByOriginal(request: WordByOriginalRequest): Result<WordResponse> {
        return safeCall(request = {
            apiService.getWordByOriginal(
                request.courseId,
                request.original
            )
        })
    }

    override suspend fun getAllWords(): Result<List<WordResponse>> {
        return Result(data = emptyList())
    }

    override suspend fun getWordsBySet(setId: String): Result<List<WordResponse>> {
        return safeCall(request = { apiService.getWordsBySet(setId) })
    }

    override suspend fun updateStatus(
        wordId: String,
        updateStatus: UpdateWordStatusRequest
    ): Result<WordResponse> {
        return safeCall(request = { apiService.updateStatus(wordId, updateStatus) })
    }

    override suspend fun delete(wordId: String) =
        safeCall<Unit>(request = { apiService.deleteWord(wordId) })

}

