package com.domain.usecase

import com.data.repository.words.IWordsRepository
import com.domain.mapper.toWordEntity
import com.presentation.model.WordUI
import com.presentation.usecases.IUpdateWordUseCase

class UpdateWordUseCase(
    private val repo: IWordsRepository,
) : IUpdateWordUseCase {
    override suspend fun invoke(newWord: WordUI) {
        repo.updateWord(newWord.toWordEntity())
    }
}