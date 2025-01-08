package com.domain.usecase.words

import com.data.repository.words.room.IWordsDaoRepository
import com.domain.mapper.toWordEntity
import com.presentation.model.WordUI
import com.presentation.usecases.words.IUpdateWordUseCase

class UpdateWordUseCase(
    private val repo: IWordsDaoRepository,
) : IUpdateWordUseCase {
    override suspend fun invoke(newWord: WordUI) {
        repo.updateWord(newWord.toWordEntity())
    }
}