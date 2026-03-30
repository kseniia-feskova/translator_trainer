package domain.usecases.sets

import data.model.sets.SetResponse

interface IAddSetUseCase {

    suspend fun invoke(
        name: String,
        isDefault: Boolean,
        courseId: String,
        listOfWords: List<String>
    ): Result<SetResponse>

}