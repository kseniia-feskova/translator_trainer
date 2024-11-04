package com.domain.usecase

import com.data.mock.model.Word
import com.data.mock.repo.IMockWordRepository

interface IAddWordUseCase {
    fun invoke(setId: String, newWord: Word)
}

class AddWordUseCase(private val repo: IMockWordRepository) : IAddWordUseCase {

    override fun invoke(setId: String, newWord: Word) {
        repo.addNewWord(setId, newWord)
    }
}