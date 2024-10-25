package com.domain.usecase

import com.data.model.WordEntity
import io.reactivex.Observable

interface IGetLastWordsUseCase {
    fun invoke(): Observable<List<WordEntity>>
}