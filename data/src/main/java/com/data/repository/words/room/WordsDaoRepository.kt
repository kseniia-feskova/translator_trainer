package com.data.repository.words.room

import android.util.Log
import com.data.model.WordEntity
import com.data.model.base.Result
import com.data.model.words.WordResponse
import com.data.model.words.add.AddWordRequest
import com.data.model.words.get.bytranslate.WordByOriginalRequest
import com.data.model.words.get.bytranslate.WordByTranslatedRequest
import com.data.model.words.update.UpdateWordStatusRequest
import com.data.repository.words.IWordRepository
import com.data.room.SetsDao
import com.data.room.WordDao
import com.data.toWordResponse
import java.util.UUID

class WordsDaoRepository(
    private val dao: WordDao,
    private val setsDao: SetsDao
) : IWordRepository {

    override suspend fun addWord(request: AddWordRequest): Result<WordResponse> {
        val wordEntity = WordEntity(
            id = UUID.randomUUID(),
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

    override suspend fun getWordsBySet(setId: UUID): Result<List<WordResponse>> {
        val set = setsDao.getSetById(setId) ?: return Result(errorMsg = "Set does not exist")
        return Result(data = set.words.map { it.toWordResponse() })
    }

    override suspend fun updateStatus(
        wordId: UUID,
        updateStatus: UpdateWordStatusRequest
    ): Result<WordResponse> {
        dao.updateWordStatus(wordId, updateStatus.status)
        val newValue = dao.getWordById(wordId) ?: return Result(errorMsg = "Word does not exist")
        return Result(data = newValue.toWordResponse())
    }

    override suspend fun delete(wordId: UUID): Result<Void> {
        dao.deleteWordById(wordId)
        return Result()
    }

}