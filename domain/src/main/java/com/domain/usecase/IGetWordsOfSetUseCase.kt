package com.domain.usecase

import com.data.model.WordEntity
import com.data.repository.words.IWordsRepository
import kotlinx.coroutines.flow.Flow

interface IGetWordsOfSetUseCase {
    fun invoke(setId: Int): Flow<List<WordEntity>>
}

class GetWordsOfSetUseCase(
    private val repository: IWordsRepository
) : IGetWordsOfSetUseCase {
    override fun invoke(setId: Int): Flow<List<WordEntity>> {

        return repository.getWordsInSet(setId)
    }

}