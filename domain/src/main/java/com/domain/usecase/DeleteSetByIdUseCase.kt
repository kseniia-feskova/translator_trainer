package com.domain.usecase

import com.data.repository.words.IWordsRepository
import com.presentation.usecases.IDeleteSetByIdUseCase

class DeleteSetByIdUseCase(private val repo: IWordsRepository) : IDeleteSetByIdUseCase {
    override suspend fun invoke(setId: Int) {
        repo.deleteSetById(setId)
    }
}