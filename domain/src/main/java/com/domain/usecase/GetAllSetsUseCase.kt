package com.domain.usecase

import com.data.repository.words.room.IWordsDaoRepository
import com.data.room.ALL_WORDS
import com.domain.mapper.toPresentation
import com.presentation.model.SetOfCards
import com.presentation.usecases.IGetAllSetsUseCase

class GetAllSetsUseCase(
    private val repo: IWordsDaoRepository,
) : IGetAllSetsUseCase {

    override suspend fun invoke(): List<SetOfCards> {
        return repo.getAllSets().map { it.toPresentation() }.sortedBy { it.title == ALL_WORDS }
    }

}