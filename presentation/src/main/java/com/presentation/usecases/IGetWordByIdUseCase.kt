package com.presentation.usecases

import com.presentation.model.WordUI

interface IGetWordByIdUseCase {

    suspend fun invoke(id: Int): WordUI?

}