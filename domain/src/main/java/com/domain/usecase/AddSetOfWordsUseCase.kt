package com.domain.usecase

import com.data.repository.words.room.IWordsDaoRepository
import com.presentation.model.SetOfCards
import com.presentation.usecases.IAddSetOfWordsUseCase

class AddSetOfWordsUseCase(
    private val repo: IWordsDaoRepository
) : IAddSetOfWordsUseCase {

    override suspend fun invoke(setOfWords: SetOfCards): Long {
        return 1L//repo.insertSet(setOfWords.toData())
    }

}