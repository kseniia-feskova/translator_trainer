package domain.usecases.auth.verify

interface IResendCodeUseCase {
    suspend fun invoke(): Result<Unit>
}