package com.domain.usecase.words

import com.data.repository.words.room.IWordsDaoRepository
import com.presentation.usecases.words.IDeleteWordUseCase
import java.util.UUID

class DeleteWordUseCase(private val repo: IWordsDaoRepository) : IDeleteWordUseCase {

    override suspend fun invoke(wordId: UUID) {
        repo.deleteWord(wordId)
    }

}