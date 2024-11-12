package com.presentation.usecases

import com.presentation.model.SetOfCards

interface IGetSetOfAllCardsUseCase {

    suspend fun invoke(): SetOfCards?

}