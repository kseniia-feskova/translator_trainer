package com.domain.usecase.words

import com.data.model.SetWordCrossRef
import com.data.model.words.add.AddWordRequest
import com.data.repository.words.api.IWordsApiRepository
import com.data.repository.words.room.IWordsDaoRepository
import com.domain.mapper.toDao
import com.domain.mapper.toNewWordEntity
import com.domain.mapper.toUI
import com.domain.token.ITokenRefresher
import com.domain.token.safeApiCallWithRefresh
import com.presentation.data.IDataStoreManager
import com.presentation.model.WordUI
import com.presentation.usecases.words.IAddWordUseCase
import com.presentation.usecases.words.IGetWordByOriginal
import com.presentation.usecases.words.IGetWordByTranslated
import java.util.UUID

class AddWordUseCase(
    private val repo: IWordsDaoRepository,
    private val apiRepo: IWordsApiRepository,
    private val prefs: IDataStoreManager,
    private val tokenRefresher: ITokenRefresher,
    private val findWordByOrigin: IGetWordByOriginal,
    private val findWordByTranslate: IGetWordByTranslated,
) : IAddWordUseCase {

    override suspend fun invoke(
        originalText: String,
        translatedText: String
    ): Result<WordUI> {
        val wordInDB = findWordByOrigin.invoke(originalText)
        if (wordInDB.isSuccess) {
            wordInDB.getOrNull()?.apply {
               return Result.success(this)
            }
        }

        val wordInDBByTranslated = findWordByTranslate.invoke(translatedText)
        if (wordInDBByTranslated.isSuccess) {
            wordInDB.getOrNull()?.apply {
                return Result.success(this)
            }
        }
        val course = prefs.getCourse()
            ?: return Result.failure(Exception("Prefs are empty. Check them, please"))
        val request = AddWordRequest(
            originalText = originalText,
            translatedText = translatedText,
            sourceLanguage = course.originalLanguage.code,
            targetLanguage = course.translateLanguage.code,
            courseId = UUID.fromString(course.id)
        )
        val response = safeApiCallWithRefresh(
            call = { apiRepo.addWord(request) },
            onTokenExpired = { tokenRefresher.refreshToken() }
        )
        val data = response.data
        return (if (response.errorMsg.isNotEmpty()) {
            if (response.errorMsg.contains("Failed to connect")) {
                Result.failure(Exception("Failed to connect"))
            } else Result.failure(Exception(response.errorMsg))
        } else if (data == null) {
            Result.failure(Exception("Empty user data"))
        } else {
            course.selectedSetId?.let { addWordToDao(setId = it, newWord = data.toDao()) }
            Result.success(data.toUI())
        })
    }

    private suspend fun addWordToDao(setId: String, newWord: WordUI) {
        //TODO refactor dao
        val wordId = repo.addNewWord(newWord.toNewWordEntity())
        if (wordId != -1L) {
            repo.addWordToAllWordsSet(newWord.id)
            repo.insertSetWordCrossRef(
                SetWordCrossRef(
                    setId = UUID.fromString(setId),
                    wordId = newWord.id
                )
            )
        }
    }
}