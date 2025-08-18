package usecase.course

import com.presentation.usecases.course.IAddCourseUseCase
import data.model.course.CourseEntity
import data.model.course.add.AddCourseRequest
import data.model.sets.AddSetRequest
import data.prefs.IDataStoreManager
import data.repository.ICourseRepository
import data.repository.ISetRepository
import domain.ALL_WORDS
import domain.token.ICheckToken
import domain.token.ITokenRefresher
import kotlinx.coroutines.flow.firstOrNull
import mapper.toData
import mapper.toUI
import presentation.model.CourseUI
import presentation.usecases.course.ICoursesOnPrefsUseCases
import java.util.UUID

class AddCourseUseCase(
    private val repo: ICourseRepository,
    private val tokenRefresher: ITokenRefresher,
    private val dataStore: IDataStoreManager,
    private val daoSets: ISetRepository,
    private val checkToken: ICheckToken,
    private val coursesOnPrefs: ICoursesOnPrefsUseCases
) : IAddCourseUseCase {

    override suspend fun invoke(course: CourseUI, needToCreateCourse: Boolean): Result<CourseUI> {
        val isGuest = dataStore.isGuest()
        return if (isGuest) {
            val allWordsSet = daoSets.addSet(
                AddSetRequest(
                    name = ALL_WORDS,
                    courseId = course.id,
                    listOfWords = emptyList(),
                    isDefault = false
                )
            )
            dataStore.saveCourse(
                course.copy(
                    allWordsId = allWordsSet.data?.id,
                    id = UUID.fromString(course.id).toString()
                )
                    .toData()
            )
            Result.success(course)
        } else {
            val userId = dataStore.listenUserId().firstOrNull()
            if (userId == null) {
                println("handleContinue: UserId is null")
                return Result.failure(Exception("UserId is null"))
            }
            if (needToCreateCourse) {
                createNewCourse(userId, course.toData())
            } else {
                dataStore.saveCourse(course.toData())
                Result.success(course)
            }
        }
    }

    private suspend fun createNewCourse(
        userId: String,
        courseUI: CourseEntity
    ): Result<CourseUI> {
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
            coursesOnPrefs.saveOne(data.toUI())
            Result.success(data.toUI())
        }
    }

}