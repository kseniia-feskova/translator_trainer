package usecase.auth

import presentation.usecases.auth.ICreateFromGuestUseCase
import presentation.usecases.course.IAddCourseUseCase
import com.presentation.usecases.words.IAddWordByApiUseCase
import data.repository.word.IWordApiRepository
import domain.CanNotCreateCourseException
import domain.CanNotCreateSetException
import domain.CanNotCreateUserException
import domain.WordDoesNotExist
import presentation.model.CourseUI
import presentation.model.SetOfCards
import presentation.usecases.auth.IRegisterUseCase
import presentation.usecases.sets.IAddSetUseCase
import presentation.usecases.sets.IGetAllSetsUseCase

class CreateFromGuestUseCase(
    private val registerUseCase: IRegisterUseCase,
    private val courseUseCase: IAddCourseUseCase,
    private val wordsRepo: IWordApiRepository,
    private val saveWord: IAddWordByApiUseCase,
    private val sets: IGetAllSetsUseCase,
    private val addSet: IAddSetUseCase
): ICreateFromGuestUseCase {

    override suspend fun invoke(email: String, password: String, course: CourseUI): Result<String> {
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
        val courseId = courseFromBack.getOrNull() ?: return Result.failure(
            CanNotCreateCourseException()
        )

        val allWordsIds = mutableListOf<String?>()
        allWords?.forEach {
            val savedWord = saveWord.invoke(
                it.originalText,
                it.translatedText
            )
            allWordsIds.add(savedWord.getOrNull()?.id)
        }

        if (allWordsIds.size != allWords?.size) return Result.failure(WordDoesNotExist())

        val allSets = sets.invoke(course.id)
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
                        listOfWords = it.words.mapNotNull { word ->
                            val index = allWords.indexOfFirst { it.id == word.id }
                            allWordsIds[index]
                        }
                    )
                )
            }
            if (setsResults.contains(Result.failure(Throwable()))) {
                return Result.failure(CanNotCreateSetException())
            }
        }
        return Result.success(userId)
    }

    private suspend fun register(email: String, password: String): Result<String> {
        return registerUseCase.invoke(
            email = email,
            username = email,
            password = password
        )
    }

    private suspend fun saveCourse(course: CourseUI): Result<String> {
        return courseUseCase.invoke(course, true).map { it.id }
    }

}