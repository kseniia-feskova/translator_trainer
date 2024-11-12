package com.domain.usecase

import com.data.repository.words.IWordsRepository
import com.domain.mapper.toWord
import com.presentation.model.WordUI
import com.presentation.usecases.IGetFilteredWordsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date

class GetFilteredWordsUseCase(private val repository: IWordsRepository) : IGetFilteredWordsUseCase {

    override fun getWordsFilteredByDateOrStatus(
        startDate: Date,
        endDate: Date
    ): Flow<List<WordUI>> {
        return repository.getWordsFilteredByDateOrStatus(startDate, endDate)
            .map { it.map { it.toWord() } }
    }
}