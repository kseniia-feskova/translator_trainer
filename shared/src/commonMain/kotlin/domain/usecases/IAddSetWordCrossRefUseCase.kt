package domain.usecases

interface IAddSetWordCrossRefUseCase {
    suspend fun invoke(wordID: String, setID: String)
}