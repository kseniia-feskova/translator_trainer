package com.domain.usecase

import com.data.model.SetOfWords
import com.data.repository.words.IWordsRepository
import com.domain.mapper.toPresentation
import com.presentation.model.SetOfCards
import com.presentation.usecases.IGetSetOfWordsUseCase

class GetSetOfWordsUseCase(private val repo: IWordsRepository) :
    IGetSetOfWordsUseCase {

    override suspend fun invoke(id: Int): SetOfCards? {
        return repo.getSetById(id)?.toPresentation()
    }

    override suspend fun invoke(name: String): SetOfCards? {
        return repo.getSetByName(name)?.toPresentation()
    }

}