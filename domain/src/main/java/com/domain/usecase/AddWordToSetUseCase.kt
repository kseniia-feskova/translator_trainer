package com.domain.usecase

import com.data.model.SetWordCrossRef
import com.data.repository.words.room.IWordsDaoRepository
import com.presentation.usecases.IAddWordToSetUseCase
import java.util.UUID

class AddWordToSetUseCase(private val repo: IWordsDaoRepository) : IAddWordToSetUseCase {

    override suspend fun invoke(setId: Int, wordId: UUID) {
        repo.insertSetWordCrossRef(SetWordCrossRef(setId, wordId))
    }

}