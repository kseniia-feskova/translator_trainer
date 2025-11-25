package data.repos

import data.model.base.Result
import data.model.sets.AddSetRequest
import data.model.sets.SetResponse
import data.repository.set.ISetDaoRepository
import data.room.SetsDao
import data.room.WordDao
import data.room.model.SetOfWords
import data.room.model.toCommon
import java.util.UUID

class SetDaoRepository(
    private val dao: SetsDao,
    private val wordsDao: WordDao
) : ISetDaoRepository {

    override suspend fun addSet(request: AddSetRequest): Result<SetResponse> {
        val setOfWords = SetOfWords(
            id = UUID.randomUUID().toString(),
            words = request.listOfWords.map { it },
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
                words = newSet.words.map { it.toCommon() }
            )
        )
    }

    override suspend fun getAllSets(courseId: String): Result<List<SetResponse>> {
        return Result(data = dao.getAllSets().map {
            SetResponse(
                id = it.set.id,
                isDefault = it.set.isDefault,
                name = it.set.name,
                words = it.words.map { word -> word.toCommon() }
            )
        })
    }
}