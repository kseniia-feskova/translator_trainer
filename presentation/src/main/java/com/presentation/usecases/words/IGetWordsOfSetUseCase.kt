package com.presentation.usecases.words

import com.presentation.model.WordUI
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface IGetWordsOfSetUseCase {

    fun invoke(setId: UUID): Flow<List<WordUI>>


    suspend fun getSingleList(setId: Int):List<WordUI>

}