package com.domain.usecase

import com.data.model.WordEntity
import com.data.repository.words.IWordsRepository

interface IGetWordByIdUseCase {
    suspend fun invoke(id: Int): WordEntity?
}

class GetWordByIdUseCase(private val repo: IWordsRepository) : IGetWordByIdUseCase {
    override suspend fun invoke(id: Int): WordEntity? {
        return repo.getWordById(id)
    }
}