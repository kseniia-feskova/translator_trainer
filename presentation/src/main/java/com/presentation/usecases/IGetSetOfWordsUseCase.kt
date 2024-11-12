package com.presentation.usecases

import com.presentation.model.SetOfCards

interface IGetSetOfWordsUseCase {

    suspend fun invoke(id: Int): SetOfCards?

    suspend fun invoke(name: String): SetOfCards?

}