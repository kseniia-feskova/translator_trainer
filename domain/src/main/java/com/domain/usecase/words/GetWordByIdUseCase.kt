package com.domain.usecase.words

import com.data.repository.words.room.IWordsDaoRepository
import com.domain.mapper.toWord
import com.presentation.model.WordUI
import com.presentation.usecases.words.IGetWordByIdUseCase
import java.util.UUID

class GetWordByIdUseCase(private val repo: IWordsDaoRepository) : IGetWordByIdUseCase {

    override suspend fun invoke(id: UUID): WordUI? {
        return repo.getWordById(id)?.toWord()
    }

}