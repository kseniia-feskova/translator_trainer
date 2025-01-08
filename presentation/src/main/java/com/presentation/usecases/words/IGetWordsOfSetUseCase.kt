package com.presentation.usecases.words

import com.presentation.model.WordUI
import kotlinx.coroutines.flow.Flow

interface IGetWordsOfSetUseCase {

    fun invoke(setId: Int): Flow<List<WordUI>>


    suspend fun getSingleList(setId: Int):List<WordUI>

}