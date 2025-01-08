package com.domain.usecase

import com.data.repository.words.room.IWordsDaoRepository
import com.presentation.usecases.IDeleteSetByIdUseCase

class DeleteSetByIdUseCase(private val repo: IWordsDaoRepository) : IDeleteSetByIdUseCase {
    override suspend fun invoke(setId: Int) {
        repo.deleteSetById(setId)
    }
}