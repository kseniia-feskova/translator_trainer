package com.domain.usecase.auth

import com.data.repository.words.IWordRepository
import com.domain.CanNotCreateCourseException
import com.domain.CanNotCreateSetException
import com.domain.CanNotCreateUserException
import com.domain.WordDoesNotExist
import com.presentation.model.CourseUI
import com.presentation.model.SetOfCards
import com.presentation.usecases.auth.ICreateFromGuestUseCase
import com.presentation.usecases.auth.IRegisterUseCase
import com.presentation.usecases.course.IAddCourseUseCase
import com.presentation.usecases.sets.IAddSetUseCase
import com.presentation.usecases.sets.IGetAllSetsUseCase
import com.presentation.usecases.words.IAddWordByApiUseCase
import java.util.UUID

class CreateFromGuestUseCase(
    private val registerUseCase: IRegisterUseCase,
    private val courseUseCase: IAddCourseUseCase,
    private val wordsRepo: IWordRepository,
    private val saveWord: IAddWordByApiUseCase,
    private val sets: IGetAllSetsUseCase,
    private val addSet: IAddSetUseCase
) : ICreateFromGuestUseCase {

    override suspend fun invoke(email: String, password: String, course: CourseUI): Result<UUID> {
        val allWords = wordsRepo.getAllWords().data
        val userFromBack = register(email, password)
        if (userFromBack.isFailure) {
            return userFromBack
        }
        val courseFromBack = saveCourse(course)
        if (courseFromBack.isFailure) {
            return courseFromBack
        }

        val userId = userFromBack.getOrNull() ?: return Result.failure(CanNotCreateUserException())
        val courseId =
            courseFromBack.getOrNull() ?: return Result.failure(CanNotCreateCourseException())

        val allWordsIds = mutableListOf<UUID?>()
        allWords?.forEach {
            val savedWord = saveWord.invoke(
                it.originalText,
                it.translatedText
            )
            allWordsIds.add(savedWord.getOrNull()?.id)
        }

        if (allWordsIds.size != allWords?.size) return Result.failure(WordDoesNotExist())

        val allSets = sets.invoke(UUID.fromString(course.id))
        val sets = allSets.getOrNull()
        val setsResults = mutableListOf<Result<SetOfCards?>>()
        if (allSets.isSuccess && !sets.isNullOrEmpty()) {
            sets.forEach {
                setsResults.add(
                    addSet.invoke(
                        name = it.title,
                        isDefault = it.isDefault,
                        courseId = courseId,
                        //TODO: bottleneck!!!
                        listOfWords = it.words.map { word ->
                            val index = allWords.indexOfFirst { it.id == word.id }
                            allWordsIds[index]
                        }.filterNotNull()
                    )
                )
            }
            if (setsResults.contains(Result.failure(Throwable()))) {
                return Result.failure(CanNotCreateSetException())
            }
        }
        return Result.success(userId)
    }

    private suspend fun register(email: String, password: String): Result<UUID> {
        return registerUseCase.invoke(
            email = email,
            username = email,
            password = password
        )
    }

    private suspend fun saveCourse(course: CourseUI): Result<UUID> {
        return courseUseCase.invoke(course, true).map { UUID.fromString(it.id) }
    }

}