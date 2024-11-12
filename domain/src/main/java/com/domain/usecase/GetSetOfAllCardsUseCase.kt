package com.domain.usecase

import com.data.repository.words.IWordsRepository
import com.data.room.ALL_WORDS
import com.domain.mapper.toPresentation
import com.presentation.model.SetOfCards
import com.presentation.usecases.IGetSetOfAllCardsUseCase

class GetSetOfAllCardsUseCase(private val repo: IWordsRepository) : IGetSetOfAllCardsUseCase {

    override suspend fun invoke(): SetOfCards? {
        return repo.getSetByName(ALL_WORDS)?.toPresentation()
    }

}