package usecase.auth

import com.presentation.usecases.auth.IDeleteUseCase

class DeleteUseCase():IDeleteUseCase  {
    override suspend fun invoke() {
        println("DeleteUseCase: invoke()")
    }
}