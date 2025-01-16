package com.domain.usecase

import com.data.repository.words.room.IWordsDaoRepository
import com.presentation.model.SetOfCards
import com.presentation.usecases.IGetSetOfCardsUseCase
import java.util.UUID

class GetSetOfCardsUseCase(private val repo: IWordsDaoRepository) : IGetSetOfCardsUseCase {

    override suspend fun invoke(setId: UUID): SetOfCards? {
        return null // repo.getSetById(setId)?.toPresentation()
    }

    override suspend fun invoke(setName: String): SetOfCards? {
        return null//repo.getSetByName(setName)?.toPresentation()
    }

}
