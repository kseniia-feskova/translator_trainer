package domain.usecases

interface IAddWordToSetUseCase {
    suspend fun invoke(setId: String, wordId: String)
}