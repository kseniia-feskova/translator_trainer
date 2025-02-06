package com.presentation.usecases.sets

import com.presentation.model.SetOfCards
import java.util.UUID

interface IGetAllSetsUseCase {

    suspend fun invoke(courseId: UUID): Result<List<SetOfCards>>

}