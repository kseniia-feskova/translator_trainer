package presentation.usecases.sets

import presentation.model.SetOfCards

interface IGetAllSetsUseCase {

    suspend fun invoke(courseId: String): Result<List<SetOfCards>>

}