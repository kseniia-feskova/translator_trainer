package com.domain.usecase

import com.data.model.WordEntity
import com.data.repository.words.IWordsRepository

interface IUpdateWordUseCase {
    suspend fun invoke(newWord: WordEntity)
}

class UpdateWordUseCase(
    private val repo: IWordsRepository,
    private val getWordByIdUseCase: GetWordByIdUseCase
) : IUpdateWordUseCase {
    override suspend fun invoke(newWord: WordEntity) {
        val originalEntity = getWordByIdUseCase.invoke(newWord.id)
        if (originalEntity != null) {
            repo.updateWord(newWord.copy(dateAdded = originalEntity.dateAdded))
        }
    }
}