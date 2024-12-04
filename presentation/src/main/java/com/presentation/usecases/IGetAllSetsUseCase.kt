package com.presentation.usecases

import com.presentation.model.SetOfCards

interface IGetAllSetsUseCase {

    suspend fun invoke(): List<SetOfCards>

}