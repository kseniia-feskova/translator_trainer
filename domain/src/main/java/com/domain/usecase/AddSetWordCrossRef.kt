package com.domain.usecase

import com.data.model.SetWordCrossRef
import com.data.repository.words.room.IWordsDaoRepository
import java.util.UUID

class AddSetWordCrossRef(
    private val repository: IWordsDaoRepository
) : com.presentation.usecases.IAddSetWordCrossRefUseCase {

    override suspend fun invoke(wordID: UUID, setID: Int) {
        repository.insertSetWordCrossRef(
            SetWordCrossRef(
                setId = setID,
                wordId = wordID
            )
        )
    }
}