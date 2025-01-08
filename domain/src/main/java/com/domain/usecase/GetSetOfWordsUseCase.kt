package com.domain.usecase

import com.data.repository.words.room.IWordsDaoRepository
import com.domain.mapper.toPresentation
import com.presentation.model.SetOfCards
import com.presentation.usecases.IGetSetOfWordsUseCase

class GetSetOfWordsUseCase(private val repo: IWordsDaoRepository) :
    IGetSetOfWordsUseCase {

    override suspend fun invoke(id: Int): SetOfCards? {
        return repo.getSetById(id)?.toPresentation()
    }

    override suspend fun invoke(name: String): SetOfCards? {
        return repo.getSetByName(name)?.toPresentation()
    }

}