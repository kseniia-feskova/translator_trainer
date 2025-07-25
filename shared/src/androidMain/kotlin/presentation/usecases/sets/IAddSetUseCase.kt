package presentation.usecases.sets

import presentation.model.SetOfCards

interface IAddSetUseCase {

    suspend fun invoke(
        name: String,
        isDefault: Boolean,
        courseId: String,
        listOfWords: List<String>
    ): Result<SetOfCards>

}