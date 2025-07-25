package presentation.usecases.auth.verify

interface IVerifyCodeUseCase {
    suspend fun invoke(code: String): Result<String>
}