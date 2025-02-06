package com.presentation.usecases.words

import com.presentation.model.WordUI
import java.util.UUID

interface IGetWordsBySetUseCase {
    suspend fun invoke(setId: UUID): Result<List<WordUI>>
}