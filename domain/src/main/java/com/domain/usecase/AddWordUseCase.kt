package com.domain.usecase

import android.util.Log
import com.data.model.SetWordCrossRef
import com.data.repository.words.IWordsRepository
import com.domain.mapper.toNewWordEntity
import com.presentation.model.WordUI
import com.presentation.usecases.IAddWordUseCase

class AddWordUseCase(private val repo: IWordsRepository) : IAddWordUseCase {

    override suspend fun invoke(setId: Int?, newWord: WordUI) {
        if (setId == null) {
            Log.e("AddWordUseCase", "Add word to all words")
            repo.addWordToAllWordsSet(newWord.toNewWordEntity())
        } else {
            val wordId = repo.addNewWord(newWord.toNewWordEntity())
            if (wordId != -1L) {
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