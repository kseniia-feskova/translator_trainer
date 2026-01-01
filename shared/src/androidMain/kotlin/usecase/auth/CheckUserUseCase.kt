package usecase.auth

import android.util.Log
import data.repository.IUserRepository
import domain.translate.ITranslateModelProvider
import mapper.toData
import mapper.toUI
import presentation.model.UserResult
import presentation.model.UserUI
import presentation.usecases.auth.ICheckUserUseCase
import presentation.usecases.course.ICoursesOnPrefsUseCases
import presentation.usecases.course.IGetAllCoursesUseCase

class CheckUserUseCase(
    private val userRepository: IUserRepository,
    private val getCourses: IGetAllCoursesUseCase,
    private val coursesPrefs: ICoursesOnPrefsUseCases,
    private val translatorProvider: ITranslateModelProvider
) : ICheckUserUseCase {

    override suspend fun invoke(userId: String): UserResult {
        val response = userRepository.getUserById(userId)
        return if (response.errorMsg.isEmpty() && response.data != null) {
            hasSelectedCourse(response.data.toUI())
        } else if (response.errorMsg == "User does not exist") {
            UserResult.New
        } else {
            UserResult.Error(response.errorMsg)
        }
    }

    private suspend fun hasSelectedCourse(user: UserUI): UserResult {
        val response = getCourses.invoke(user.userId)
        if (!response.isSuccess) {
            UserResult.Error(response.exceptionOrNull()?.message ?: "Common failure")
        }
        val courses = response.getOrNull() ?: return UserResult.New
        if (courses.size != 1) {
            coursesPrefs.saveAll(courses)
            return UserResult.New
        } else {
            coursesPrefs.saveOne(courses.first())
            translatorProvider.downloadModel(
                courses.first().originalLanguage.toData(),
                courses.first().translateLanguage.toData()
            ) {
                Log.e("AuthVM", "Download model error $it")
            }
            return UserResult.Existing(user)
        }
    }
}