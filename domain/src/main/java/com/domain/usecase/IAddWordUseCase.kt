package com.domain.usecase

import android.util.Log
import com.data.mock.model.Status
import com.data.mock.model.Word
import com.data.model.SetWordCrossRef
import com.data.model.WordEntity
import com.data.model.WordStatus
import com.data.repository.words.IWordsRepository
import java.util.Date

interface IAddWordUseCase {
    suspend fun invoke(setId: Int? = null, newWord: Word)
}

class AddWordUseCase(private val repo: IWordsRepository) : IAddWordUseCase {
    override suspend fun invoke(setId: Int?, newWord: Word) {
        if (setId == null) {
            Log.e("AddWordUseCase", "Add word to all words")
            repo.addWordToAllWordsSet(newWord.toWordEntity())
        } else {
            val wordId = repo.addNewWord(newWord.toWordEntity())
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

fun Word.toWordEntity(): WordEntity {
    return WordEntity(
        original = this.text,
        translation = this.translate,
        status = this.status.toWordStatus(),
        dateAdded = Date()
    )
}

fun Status.toWordStatus(): WordStatus {
    return when (this) {
        Status.NEW -> WordStatus.NEW
        Status.KNOW -> WordStatus.LEARNED
        Status.LEARNING -> WordStatus.IN_PROGRESS
        Status.GOOD_LEARNING -> WordStatus.IN_GOOD_PROGRESS
    }
}

fun WordStatus.toStatus(): Status {
    return when (this) {
        WordStatus.NEW -> Status.NEW
        WordStatus.LEARNED -> Status.KNOW
        WordStatus.IN_PROGRESS -> Status.LEARNING
        WordStatus.IN_GOOD_PROGRESS -> Status.GOOD_LEARNING
    }
}