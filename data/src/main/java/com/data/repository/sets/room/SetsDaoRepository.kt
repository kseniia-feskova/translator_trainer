package com.data.repository.sets.room

import com.data.model.base.Result
import com.data.model.sets.AddSetRequest
import com.data.model.sets.SetOfWords
import com.data.model.sets.SetResponse
import com.data.repository.sets.ISetRepository
import com.data.room.SetsDao
import com.data.room.WordDao
import java.util.UUID

class SetsDaoRepository(
    private val dao: SetsDao,
    private val wordsDao: WordDao
) : ISetRepository {

    override suspend fun addSet(request: AddSetRequest): Result<SetResponse> {
        val setOfWords = SetOfWords(
            id = UUID.randomUUID(),
            words = request.listOfWords.map { it.toString() },
            name = request.name,
            isDefault = request.isDefault
        )
        dao.insertSet(setOfWords)
        request.listOfWords.forEach {
            wordsDao.addWordToSet(wordId = it, setId = setOfWords.id)
        }
        val newSet = dao.getSetById(setOfWords.id) ?: return Result(errorMsg = "Set does not exist")
        return Result(
            data = SetResponse(
                id = newSet.set.id,
                isDefault = newSet.set.isDefault,
                name = newSet.set.name,
                words = newSet.words
            )
        )
    }

    override suspend fun getAllSets(courseId: UUID): Result<List<SetResponse>> {
        return Result(data = dao.getAllSets().map {
            SetResponse(
                id = it.set.id,
                isDefault = it.set.isDefault,
                name = it.set.name,
                words = it.words
            )
        })
    }
}