package com.domain.usecase

import com.data.model.SetOfWords
import com.data.repository.words.IWordsRepository

interface IGetSetOfWordsUseCase {

    suspend fun invoke(id: Int): SetOfWords?

    suspend fun invoke(name: String): SetOfWords?

}

class GetSetOfWordsUseCase(private val repo: IWordsRepository) : IGetSetOfWordsUseCase {

    override suspend fun invoke(id: Int): SetOfWords? {
        return repo.getSetById(id)
    }

    override suspend fun invoke(name: String): SetOfWords? {
        return repo.getSetByName(name)
    }

}