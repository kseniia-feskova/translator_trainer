package com.presentation.usecases

import com.presentation.model.SetOfCards


interface IAddSetOfWordsUseCase {
    suspend fun invoke(setOfWords: SetOfCards): Long
}