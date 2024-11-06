package com.domain.usecase

import com.data.model.WordEntity
import com.data.repository.words.IWordsRepository
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface IGetFilteredWordsUseCase {
    fun getWordsFilteredByDateOrStatus(
        startDate: Date,
        endDate: Date
    ): Flow<List<WordEntity>>
}

class GetFilteredWordsUseCase(
    private val repository: IWordsRepository
) : IGetFilteredWordsUseCase {

    override fun getWordsFilteredByDateOrStatus(
        startDate: Date,
        endDate: Date
    ): Flow<List<WordEntity>> {
        return repository.getWordsFilteredByDateOrStatus(
            startDate,
            endDate
        )
    }
}