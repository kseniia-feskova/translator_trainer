package com.domain.usecase

import com.data.model.SetWordCrossRef
import com.data.repository.words.IWordsRepository

class AddSetWordCrossRef(
    private val repository: IWordsRepository
) : com.presentation.usecases.IAddSetWordCrossRefUseCase {

    override suspend fun invoke(wordID: Int, setID: Int) {
        repository.insertSetWordCrossRef(
            SetWordCrossRef(
                setId = setID,
                wordId = wordID
            )
        )
    }
}