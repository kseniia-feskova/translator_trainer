package domain.usecases.sets

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import domain.usecases.course.ICoursesOnPrefsUseCases

class GetAllWordsIdUseCase(
    private val coursePrefs: ICoursesOnPrefsUseCases
) : IGetAllWordsIdUseCase {

    override fun invoke(): Flow<String?> = flow {
        emit(coursePrefs.getCourse()?.allWordsId)
    }

}