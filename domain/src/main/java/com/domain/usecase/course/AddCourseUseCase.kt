package com.domain.usecase.course

import android.util.Log
import translator.data.model.course.add.AddCourseRequest
import translator.data.model.sets.AddSetRequest
import translator.data.prefs.IDataStoreManager
import translator.data.repository.course.ICourseRepository
import translator.data.repository.sets.ISetRepository
import translator.data.room.ALL_WORDS
import com.domain.mapper.toData
import com.domain.mapper.toUI
import com.domain.token.ITokenRefresher
import com.domain.token.safeApiCallWithRefresh
import com.presentation.model.CourseUI
import com.presentation.usecases.course.IAddCourseUseCase
import kotlinx.coroutines.flow.firstOrNull
import java.util.UUID

class AddCourseUseCase(
    private val repo: ICourseRepository,
    private val tokenRefresher: ITokenRefresher,
    private val dataStore: IDataStoreManager,
    private val daoSets: ISetRepository
) : IAddCourseUseCase {

    override suspend fun invoke(course: CourseUI, needToCreateCourse: Boolean): Result<CourseUI> {
        val isGuest = dataStore.isGuest()
        return if (isGuest) {
            val allWordsSet = daoSets.addSet(
                AddSetRequest(
                    name = ALL_WORDS,
                    courseId = UUID.fromString(course.id),
                    listOfWords = emptyList(),
                    isDefault = false
                )
            )
            dataStore.saveCourse(
                course.toData().copy(
                    allWordsId = allWordsSet.data?.id
                )
            )
            Result.success(course)
        } else {
            val userId = dataStore.listenUserId().firstOrNull()
            if (userId == null) {
                Log.e("handleContinue", "UserId is null")
                return Result.failure(Exception("UserId is null"))
            }
            if (needToCreateCourse) {
                createNewCourse(userId, course)
            } else {
                dataStore.saveCourse(course.toData())
                Result.success(course)
            }
        }
    }

    private suspend fun createNewCourse(userId: UUID, courseUI: CourseUI): Result<CourseUI> {
        val request = AddCourseRequest(
            name = "${courseUI.originalLanguage.name} - ${courseUI.translateLanguage.name}",
            userId = userId,
            sourceLanguage = courseUI.originalLanguage.code,
            targetLanguage = courseUI.translateLanguage.code
        )
        val response = safeApiCallWithRefresh(
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
            Result.success(data.toUI())
        }
    }

}