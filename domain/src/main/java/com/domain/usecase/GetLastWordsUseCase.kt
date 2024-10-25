package com.domain.usecase

import com.data.model.WordEntity
import com.data.repository.words.IWordsRepository
import io.reactivex.Observable

class GetLastWordsUseCase(
    private val repository: IWordsRepository
) : IGetLastWordsUseCase {
    override fun invoke(): Observable<List<WordEntity>> {
        return repository.getLastWord(WORDS_COUNT)
    }

    companion object {
        const val WORDS_COUNT = 5
    }
}