package com.domain.usecase

import com.data.repository.words.room.IWordsDaoRepository
import com.domain.mapper.toData
import com.presentation.model.SetOfCards
import com.presentation.usecases.IAddSetOfWordsUseCase

class AddSetOfWordsUseCase(
    private val repo: IWordsDaoRepository
) : IAddSetOfWordsUseCase {

    override suspend fun invoke(setOfWords: SetOfCards): Long {
        return repo.insertSet(setOfWords.toData())
    }

}