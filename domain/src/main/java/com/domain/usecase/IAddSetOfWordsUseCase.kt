package com.domain.usecase

import com.data.model.SetOfWords
import com.data.repository.words.IWordsRepository

interface IAddSetOfWordsUseCase {
    suspend fun invoke(setOfWords: SetOfWords): Long
}

class AddSetOfWordsUseCase(
    private val repo: IWordsRepository
) : IAddSetOfWordsUseCase {

    override suspend fun invoke(setOfWords: SetOfWords): Long {
        return repo.insertSet(setOfWords)
    }

}