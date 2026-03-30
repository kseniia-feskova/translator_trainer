package domain.usecases.words

interface IDeleteWordUseCase {

    suspend fun invoke(wordId: String): Result<Unit>

}