package com.domain.usecase

import com.data.model.SetWordCrossRef
import com.data.repository.words.IWordsRepository

interface IAddSetWordCrossRef {
    suspend fun invoke(wordID: Int, setID: Int)
}

class AddSetWordCrossRef(
    private val repository: IWordsRepository
) : IAddSetWordCrossRef {
    override suspend fun invoke(wordID: Int, setID: Int) {
        repository.insertSetWordCrossRef(
            SetWordCrossRef(
                setId = setID,
                wordId = wordID
            )
        )
    }
}