package com.domain.usecase

import com.data.model.SetOfWords
import com.data.repository.words.IWordsRepository

interface IAddSetOfWordsUseCase {
    suspend fun invoke(setOfWords: SetOfWords)
}

class AddSetOfWordsUseCase(
    private val repo: IWordsRepository
) : IAddSetOfWordsUseCase {

    override suspend fun invoke(setOfWords: SetOfWords) {
        repo.insertSet(setOfWords)
    }

}