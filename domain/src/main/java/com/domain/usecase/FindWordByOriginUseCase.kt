package com.domain.usecase

import com.data.repository.words.IWordsRepository
import com.domain.mapper.toWord
import com.presentation.model.WordUI
import com.presentation.usecases.IFindWordByOriginUseCase
import com.presentation.usecases.IFindWordByTranslatedUseCase

class FindWordByOriginUseCase(val repo: IWordsRepository) : IFindWordByOriginUseCase {

    override suspend fun invoke(origin: String): WordUI? {
        return repo.findWordByOrigin(origin)?.toWord()
    }

}

class FindWordByTranslatedUseCase(val repo: IWordsRepository) : IFindWordByTranslatedUseCase {

    override suspend fun invoke(translated: String): WordUI? {
        return repo.findWordByTranslated(translated)?.toWord()
    }

}