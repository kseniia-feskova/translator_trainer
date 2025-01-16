package com.presentation.usecases

import com.presentation.model.SetOfCards
import java.util.UUID

interface IGetSetOfCardsUseCase {

    suspend fun invoke(setId: UUID): SetOfCards?

    suspend fun invoke(setName: String): SetOfCards?

}