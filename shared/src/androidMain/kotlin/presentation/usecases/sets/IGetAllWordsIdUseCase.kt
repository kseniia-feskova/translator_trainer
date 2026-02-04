package presentation.usecases.sets

import kotlinx.coroutines.flow.Flow

interface IGetAllWordsIdUseCase {
    fun invoke(): Flow<String?>
}