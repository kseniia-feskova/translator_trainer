package com.domain.usecase.words

import com.data.model.SetWordCrossRef
import com.data.model.words.AddWordRequest
import com.data.repository.words.api.IWordsApiRepository
import com.data.repository.words.room.IWordsDaoRepository
import com.domain.mapper.toNewWordEntity
import com.domain.mapper.toUI
import com.presentation.data.IDataStoreManager
import com.presentation.model.WordUI
import com.presentation.usecases.words.IAddWordUseCase
import kotlinx.coroutines.flow.firstOrNull

class AddWordUseCase(
    private val repo: IWordsDaoRepository,
    private val apiRepo: IWordsApiRepository,
    private val prefs: IDataStoreManager
) : IAddWordUseCase {

    override suspend fun invoke(setId: Int?, newWord: WordUI): Result<WordUI> {
        val wordId = repo.addNewWord(newWord.toNewWordEntity())
        if (wordId != -1L) {
            repo.addWordToAllWordsSet(newWord.id)
            if (setId != null) {
                repo.insertSetWordCrossRef(
                    SetWordCrossRef(
                        setId = setId,
                        wordId = newWord.id
                    )
                )
            }
            return addWordToApi(newWord)
        } else return Result.failure(Exception("Can not create word"))
    }

    private suspend fun addWordToApi(newWord: WordUI): Result<WordUI> {
        val sourceLanguage = prefs.getOriginalLanguage()
        val targetLanguage = prefs.getResultLanguage()
        val userId = prefs.listenUserId().firstOrNull()
        if (sourceLanguage == null || targetLanguage == null || userId == null) return Result.failure(
            Exception("Prefs are empty. Check them, please")
        )
        val response = apiRepo.addWord(
            AddWordRequest(
                originalText = newWord.originalText,
                translatedText = newWord.resText,
                sourceLanguage = sourceLanguage.code,
                targetLanguage = targetLanguage.code,
                userId = userId
            )
        )
        val data = response.data
        return (if (response.errorMsg.isNotEmpty()) {
            Result.failure(Exception(response.errorMsg))
        } else if (data == null) {
            Result.failure(Exception("Empty user data"))
        } else Result.success(data.toUI()))
    }
}

