package com.presentation.usecases.words

import com.presentation.model.WordUI
import java.util.UUID

interface IGetWordByIdUseCase {

    suspend fun invoke(id: UUID): WordUI?

}