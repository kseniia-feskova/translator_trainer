package com.domain.usecase

import com.data.repository.words.IWordsRepository
import com.domain.mapper.toWord
import com.presentation.model.WordUI
import com.presentation.usecases.IGetWordsOfSetUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class GetWordsOfSetUseCase(private val repo: IWordsRepository) : IGetWordsOfSetUseCase {

    override fun invoke(setId: Int): Flow<List<WordUI>> {
        return repo.getWordsInSet(setId).map { it.words.map { it.toWord() } }
    }

    override suspend fun getSingleList(setId: Int): List<WordUI> {
        return repo.getWordsInSet(setId)
            .map { it.words.map { word -> word.toWord() } }
            .first()
    }

}