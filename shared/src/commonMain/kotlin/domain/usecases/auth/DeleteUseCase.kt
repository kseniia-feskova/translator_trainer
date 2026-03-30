package domain.usecases.auth

class DeleteUseCase():IDeleteUseCase  {
    override suspend fun invoke() {
        println("DeleteUseCase: invoke()")
    }
}