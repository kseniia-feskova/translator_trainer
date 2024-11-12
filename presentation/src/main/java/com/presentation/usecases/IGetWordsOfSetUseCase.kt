package com.presentation.usecases

import com.presentation.model.WordUI
import kotlinx.coroutines.flow.Flow

interface IGetWordsOfSetUseCase {

    fun invoke(setId: Int): Flow<List<WordUI>>

}