package com.domain.usecase

import com.data.repository.words.room.IWordsDaoRepository
import com.domain.mapper.toPresentation
import com.presentation.model.SetOfCards
import com.presentation.usecases.IGetSetOfCardsUseCase

class GetSetOfCardsUseCase(private val repo: IWordsDaoRepository) : IGetSetOfCardsUseCase {

    override suspend fun invoke(setId: Int): SetOfCards? {
        return repo.getSetById(setId)?.toPresentation()
    }

    override suspend fun invoke(setName: String): SetOfCards? {
        return repo.getSetByName(setName)?.toPresentation()
    }

}
