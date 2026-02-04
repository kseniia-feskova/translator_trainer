package usecase.sets

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import presentation.usecases.course.ICoursesOnPrefsUseCases
import presentation.usecases.sets.IGetAllWordsIdUseCase

class GetAllWordsIdUseCase(
    private val coursePrefs: ICoursesOnPrefsUseCases
) : IGetAllWordsIdUseCase {

    override fun invoke(): Flow<String?> = flow {
        emit(coursePrefs.getCourse()?.allWordsId)
    }

}