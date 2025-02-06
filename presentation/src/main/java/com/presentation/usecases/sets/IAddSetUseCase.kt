package com.presentation.usecases.sets

import com.presentation.model.SetOfCards
import java.util.UUID

interface IAddSetUseCase {

    suspend fun invoke(
        name: String,
        isDefault: Boolean,
        courseId: UUID,
        listOfWords: List<UUID>
    ): Result<SetOfCards>

}