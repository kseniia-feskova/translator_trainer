package data.repos

import android.util.Log
import data.model.base.Result
import data.model.words.WordResponse
import data.model.words.add.AddWordRequest
import data.model.words.get.bytranslate.WordByOriginalRequest
import data.model.words.get.bytranslate.WordByTranslatedRequest
import data.model.words.update.UpdateWordStatusRequest
import data.room.SetsDao
import data.room.WordDao
import data.room.model.WordEntity
import mapper.toWordResponse
import java.util.UUID

class WordsDaoRepository(
    private val dao: WordDao,
    private val setsDao: SetsDao
) : data.repository.IWordRepository {

    override suspend fun addWord(request: AddWordRequest): Result<WordResponse> {
        val wordEntity = WordEntity(
            id = UUID.randomUUID().toString(),
            originalText = request.originalText,
            translatedText = request.translatedText,
            sourceLanguage = request.sourceLanguage,
            targetLanguage = request.targetLanguage,
            status = request.status,
            courseId = request.courseId
        )
        Log.e("WordsDaoRepo", "Add word : $wordEntity")
        dao.addWordToAllWordsSet(wordEntity)
        return Result(data = wordEntity.toWordResponse())
    }

    override suspend fun getWordByTranslated(request: WordByTranslatedRequest): Result<WordResponse> {
        val inDao = dao.getWordByOriginal(request.translate)
        return if (inDao == null) {
            Result(errorMsg = "Word does not exist")
        } else Result(data = inDao.toWordResponse())
    }

    override suspend fun getWordByOriginal(request: WordByOriginalRequest): Result<WordResponse> {
        val inDao = dao.getWordByOriginal(request.original)
        return if (inDao == null) {
            Result(errorMsg = "Word does not exist")
        } else Result(data = inDao.toWordResponse())
    }

    override suspend fun getAllWords(): Result<List<WordResponse>> {
        return Result(dao.getAllWords()?.map { it.toWordResponse() })
    }

    override suspend fun getWordsBySet(setId: String): Result<List<WordResponse>> {
        val set = setsDao.getSetById(setId) ?: return Result(errorMsg = "Set does not exist")
        return Result(data = set.words.map { it.toWordResponse() })
    }

    override suspend fun updateStatus(
        wordId: String,
        updateStatus: UpdateWordStatusRequest
    ): Result<WordResponse> {
        dao.updateWordStatus(wordId, updateStatus.status)
        val newValue = dao.getWordById(wordId) ?: return Result(errorMsg = "Word does not exist")
        return Result(data = newValue.toWordResponse())
    }

    override suspend fun delete(wordId: String): Result<Void> {
        dao.deleteWordById(wordId)
        return Result()
    }

}