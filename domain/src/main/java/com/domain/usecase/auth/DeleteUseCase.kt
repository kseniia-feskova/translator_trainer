package com.domain.usecase.auth

import android.util.Log
import com.presentation.usecases.auth.IDeleteUseCase

class DeleteUseCase : IDeleteUseCase {
    override suspend fun invoke() {
        Log.e("DeleteUseCase", "invoke()")
    }
}