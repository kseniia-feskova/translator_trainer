package domain.usecases.auth

import data.model.course.CourseEntity
import data.model.sets.SetResponse
import data.repository.word.IWordDaoRepository
import domain.ALL_WORDS
import domain.CanNotCreateCourseException
import domain.CanNotCreateSetException
import domain.CanNotCreateUserException
import domain.WordDoesNotExist
import domain.usecases.course.IAddCourseUseCase
import domain.usecases.sets.IAddSetUseCase
import domain.usecases.sets.IGetAllSetsUseCase
import domain.usecases.words.IAddWordByApiUseCase

class CreateFromGuestUseCase(
    private val registerUseCase: IRegisterUseCase,
    private val courseUseCase: IAddCourseUseCase,
    private val wordsRepo: IWordDaoRepository,
    private val saveWord: IAddWordByApiUseCase,
    private val getAllSets: IGetAllSetsUseCase,
    private val addSet: IAddSetUseCase
) : ICreateFromGuestUseCase {

    //TODO refactor is needed
    override suspend fun invoke(email: String, password: String, course: CourseEntity): Result<String> {
        val allWords = wordsRepo.getAllWords().data
        val allSets = getAllSets.invoke()
        val sets = allSets.getOrNull()
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
        if (sets?.size == 1) {
            return Result.success(userId)
        }
        val setsResults = mutableListOf<Result<SetResponse?>>()
        if (allSets.isSuccess && !sets.isNullOrEmpty()) {
            sets.forEach {
                if (it.name != ALL_WORDS) {
                    setsResults.add(
                        addSet.invoke(
                            name = it.name,
                            isDefault = it.isDefault,
                            courseId = courseId,
                            //TODO: bottleneck!!!
                            listOfWords = it.words.mapNotNull { word ->
                                val index =
                                    allWords.indexOfFirst { it.translatedText == word.translatedText }
                                if (index > 0 && index < allWordsIds.size) {
                                    allWordsIds[index]
                                } else null
                            }
                        )
                    )
                }
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

    private suspend fun saveCourse(course: CourseEntity): Result<String> {
        return courseUseCase.invoke(course, true).map { it.id }
    }

}