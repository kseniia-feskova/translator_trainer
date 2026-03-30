package domain.usecases

import java.util.UUID

interface IAddWordToSetUseCase {
    suspend fun invoke(setId: UUID, wordId: UUID)
}