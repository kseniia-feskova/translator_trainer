package com.domain.usecase

import com.data.mock.model.SetOfCards
import com.data.mock.repo.MockWordRepository

interface IGetSetOfCardsUseCase {

    fun invoke(setId: String): SetOfCards

}

class GetSetOfCardsUseCase(private val repo: MockWordRepository) : IGetSetOfCardsUseCase {

    override fun invoke(setId: String): SetOfCards {
        return repo.getSetOfWords(setId)
    }

}