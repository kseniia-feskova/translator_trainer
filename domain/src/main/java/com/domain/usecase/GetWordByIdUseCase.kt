package com.domain.usecase

import com.data.repository.words.IWordsRepository
import com.domain.mapper.toWord
import com.presentation.model.WordUI
import com.presentation.usecases.IGetWordByIdUseCase

class GetWordByIdUseCase(private val repo: IWordsRepository) : IGetWordByIdUseCase {

    override suspend fun invoke(id: Int): WordUI? {
        return repo.getWordById(id)?.toWord()
    }

}