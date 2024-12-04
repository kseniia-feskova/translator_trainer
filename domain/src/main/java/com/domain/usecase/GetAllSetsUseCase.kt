package com.domain.usecase

import com.data.repository.words.IWordsRepository
import com.data.room.ALL_WORDS
import com.domain.mapper.toPresentation
import com.presentation.model.SetOfCards
import com.presentation.usecases.IGetAllSetsUseCase
import com.presentation.usecases.IGetWordsOfSetUseCase
import kotlinx.coroutines.flow.first

class GetAllSetsUseCase(
    private val repo: IWordsRepository,
    private val getWords: IGetWordsOfSetUseCase
) : IGetAllSetsUseCase {

    override suspend fun invoke(): List<SetOfCards> {
        return repo.getAllSets().map { it.toPresentation() }.map {
            val id = it.id
            if (it.title == ALL_WORDS && id != null) {
                it.copy(setOfWords = getWords.invoke(id).first().toSet())
            } else it
        }.sortedBy {
            it.title == ALL_WORDS
        }
    }

}