package com.presentation.usecases.words

import com.presentation.model.WordUI
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface IGetFilteredWordsUseCase {

    fun getWordsFilteredByDateOrStatus(
        startDate: Date,
        endDate: Date
    ): Flow<List<WordUI>>

}