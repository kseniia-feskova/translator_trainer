package com.domain.usecase

import com.data.model.SetOfWords
import com.data.repository.words.IWordsRepository

interface IGetSetOfCardsUseCase {

    suspend fun invoke(setId: Int): SetOfWords?

    suspend fun invoke(setName: String): SetOfWords?

}

class GetSetOfCardsUseCase(private val repo: IWordsRepository) : IGetSetOfCardsUseCase {

    override suspend fun invoke(setId: Int): SetOfWords? {
        return repo.getSetById(setId)
    }

    override suspend fun invoke(setName: String): SetOfWords? {
        return repo.getSetByName(setName)
    }

}
