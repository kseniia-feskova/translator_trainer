package com.presentation.usecases

import com.presentation.model.SetOfCards
import java.util.UUID

interface IGetAllSetsUseCase {

    suspend fun invoke(courseId: UUID): Result<List<SetOfCards>>

}