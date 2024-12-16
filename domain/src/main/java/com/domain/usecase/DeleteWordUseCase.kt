package com.domain.usecase

import com.data.repository.words.IWordsRepository
import com.presentation.usecases.IDeleteWordUseCase

class DeleteWordUseCase(private val repo: IWordsRepository) : IDeleteWordUseCase {

    override suspend fun invoke(wordId: Int) {
        repo.deleteWord(wordId)
    }

}