package com.domain.usecase

import com.data.model.SetOfWords
import com.data.repository.words.IWordsRepository
import com.data.room.ALL_WORDS

interface IGetSetOfAllCardsUseCase {

    suspend fun invoke(): SetOfWords?

}

class GetSetOfAllCardsUseCase(private val repo: IWordsRepository) : IGetSetOfAllCardsUseCase {

    override suspend fun invoke(): SetOfWords? {
        return repo.getSetByName(ALL_WORDS)
    }

}