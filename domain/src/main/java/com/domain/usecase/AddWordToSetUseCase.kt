package com.domain.usecase

import com.data.model.SetWordCrossRef
import com.data.repository.words.IWordsRepository
import com.presentation.usecases.IAddWordToSetUseCase

class AddWordToSetUseCase(private val repo: IWordsRepository) : IAddWordToSetUseCase {

    override suspend fun invoke(setId: Int, wordId: Int) {
        repo.insertSetWordCrossRef(SetWordCrossRef(setId, wordId))
    }

}