package com.presentation.usecases.words

import com.presentation.model.Level
import com.presentation.model.WordUI
import java.util.UUID

interface IUpdateStatusUseCase {

    suspend fun invoke(wordId: UUID, level: Level): Result<WordUI>

}