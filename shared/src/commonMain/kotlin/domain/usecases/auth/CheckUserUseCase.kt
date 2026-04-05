package domain.usecases.auth

import data.model.user.UserEntity
import data.model.user.UserResult
import data.repository.IUserRepository
import data.translate.Language
import data.translate.languageOf
import domain.Logger
import domain.translate.ITranslateModelProvider
import domain.usecases.course.ICoursesOnPrefsUseCases
import domain.usecases.course.IGetAllCoursesUseCase

class CheckUserUseCase(
    private val userRepository: IUserRepository,
    private val getCourses: IGetAllCoursesUseCase,
    private val coursesPrefs: ICoursesOnPrefsUseCases,
    private val translatorProvider: ITranslateModelProvider
) : ICheckUserUseCase {

    override suspend fun invoke(userId: String): UserResult {
        val response = userRepository.getUserById(userId)
        return if (response.errorMsg.isEmpty() && response.data != null) {
            hasSelectedCourse(response.data)
        } else if (response.errorMsg == "User does not exist") {
            UserResult.New
        } else {
            UserResult.Error(response.errorMsg)
        }
    }

    private suspend fun hasSelectedCourse(user: UserEntity): UserResult {
        val response = getCourses.invoke(user.id)
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
                languageOf(courses.first().sourceLanguage)?: Language.AUTO,
                languageOf(courses.first().targetLanguage) ?: Language.AUTO
            ) {
                Logger.e("AuthVM", "Download model error $it")
            }
            return UserResult.Existing(user)
        }
    }
}