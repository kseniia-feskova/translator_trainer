package domain.usecases.course

import data.model.course.CourseEntity
import data.model.course.add.AddCourseRequest
import data.model.sets.AddSetRequest
import data.prefs.IDataStoreManager
import data.repository.course.ICourseRepository
import data.repository.set.ISetDaoRepository
import domain.utils.ALL_WORDS
import domain.token.ICheckToken
import domain.token.ITokenRefresher

class AddCourseUseCase(
    private val repo: ICourseRepository,
    private val tokenRefresher: ITokenRefresher,
    private val dataStore: IDataStoreManager,
    private val daoSets: ISetDaoRepository,
    private val checkToken: ICheckToken,
    private val coursesOnPrefs: ICoursesOnPrefsUseCases
) : IAddCourseUseCase {

    override suspend fun invoke(course: CourseEntity, needToCreateCourse: Boolean): Result<CourseEntity> {
        return if (dataStore.isGuest() || dataStore.isOfflineMode()) {
            val allWordsSet = daoSets.addSet(
                AddSetRequest(
                    name = ALL_WORDS,
                    courseId = course.id,
                    listOfWords = emptyList(),
                    isDefault = false
                )
            )
            dataStore.saveCourse(
                course.copy(allWordsId = allWordsSet.data?.id)
            )
            Result.success(course)
        } else {
            val userId = dataStore.getUserId()
            if (userId == null) {
                println("handleContinue: UserId is null")
                return Result.failure(Exception("UserId is null"))
            }
            if (needToCreateCourse) {
                createNewCourse(userId, course)
            } else {
                dataStore.saveCourse(course)
                Result.success(course)
            }
        }
    }

    private suspend fun createNewCourse(
        userId: String,
        courseUI: CourseEntity
    ): Result<CourseEntity> {
        val request = AddCourseRequest(
            name = "${courseUI.sourceLanguage} - ${courseUI.targetLanguage}",
            userId = userId,
            sourceLanguage = courseUI.sourceLanguage,
            targetLanguage = courseUI.targetLanguage
        )
        val response = checkToken.safeApiCallWithRefresh(
            call = { repo.addCourse(request) },
            onTokenExpired = { tokenRefresher.refreshToken() }
        )
        val data = response.data
        return if (response.errorMsg.isNotEmpty()) {
            Result.failure(Exception(response.errorMsg))
        } else if (data == null) {
            Result.failure(Exception("Empty user data"))
        } else {
            dataStore.saveCourse(data)
            coursesOnPrefs.saveOne(data)
            Result.success(data)
        }
    }

}