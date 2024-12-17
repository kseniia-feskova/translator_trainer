package com.domain.usecase

import com.data.model.SetWordCrossRef
import com.data.repository.words.IWordsRepository
import com.domain.mapper.toNewWordEntity
import com.presentation.model.WordUI
import com.presentation.usecases.IAddWordUseCase

class AddWordUseCase(private val repo: IWordsRepository) : IAddWordUseCase {

    override suspend fun invoke(setId: Int?, newWord: WordUI) {
        val wordId = repo.addNewWord(newWord.toNewWordEntity())
        if (wordId != -1L) {
            repo.addWordToAllWordsSet(wordId.toInt())
            if (setId != null) {
                repo.insertSetWordCrossRef(
                    SetWordCrossRef(
                        setId = setId,
                        wordId = wordId.toInt()
                    )
                )
            }
        }
    }
}