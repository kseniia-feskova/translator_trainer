package com.domain.usecase.words

import com.data.repository.words.room.IWordsDaoRepository
import com.domain.mapper.toWord
import com.presentation.model.WordUI
import com.presentation.usecases.words.IFindWordByOriginUseCase
import com.presentation.usecases.words.IFindWordByTranslatedUseCase

class FindWordByOriginUseCase(val repo: IWordsDaoRepository) : IFindWordByOriginUseCase {

    override suspend fun invoke(origin: String): WordUI? {
        return repo.findWordByOrigin(origin)?.toWord()
    }

}

class FindWordByTranslatedUseCase(val repo: IWordsDaoRepository) : IFindWordByTranslatedUseCase {

    override suspend fun invoke(translated: String): WordUI? {
        return repo.findWordByTranslated(translated)?.toWord()
    }

}