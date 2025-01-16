package com.domain.usecase

import com.data.repository.words.room.IWordsDaoRepository
import com.presentation.model.SetOfCards
import com.presentation.usecases.IGetSetOfAllCardsUseCase

class GetSetOfAllCardsUseCase(private val repo: IWordsDaoRepository) : IGetSetOfAllCardsUseCase {

    override suspend fun invoke(): SetOfCards? {
        return null//repo.getSetByName(ALL_WORDS)?.toPresentation()
    }

}