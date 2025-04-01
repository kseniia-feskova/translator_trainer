package com.domain.usecase.auth

import com.data.repository.words.IWordRepository
import com.presentation.model.CourseUI
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

    override suspend fun invoke(email: String, password: String, course: CourseUI) {
        val allWords = wordsRepo.getAllWords().data
        //TODO: handle errors
        register(email, password) ?: return
        val courseFromBack = saveCourse(course) ?: return
        val allWordsIds = mutableListOf<UUID?>()
        allWords?.forEach {
            val savedWord = saveWord.invoke(
                it.originalText,
                it.translatedText
            )
            allWordsIds.add(savedWord.getOrNull()?.id)
        }

        val allSets = sets.invoke(UUID.fromString(course.id))
        val sets = allSets.getOrNull()
        if (allSets.isSuccess && !sets.isNullOrEmpty()) {
            sets.forEach {
                addSet.invoke(
                    name = it.title,
                    isDefault = it.isDefault,
                    courseId = UUID.fromString(courseFromBack.id),
                    //TODO: bottleneck!!!
                    listOfWords = it.words.map { word ->
                        val index = allWords?.indexOfFirst { it.id == word.id }
                        if (index != null) {
                            allWordsIds[index]
                        } else word.id
                    }.filterNotNull()
                )
            }
        }
    }

    private suspend fun register(email: String, password: String): UUID? {
        val result = registerUseCase.invoke(
            email = email,
            username = email,
            password = password
        )
        return if (result.isSuccess) {
            result.getOrNull()
        } else {
            null
        }
    }

    private suspend fun saveCourse(course: CourseUI): CourseUI? {
        val result = courseUseCase.invoke(course, true)
        return if (result.isSuccess) {
            result.getOrNull()
        } else {
            null
        }
    }

}