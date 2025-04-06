package translator.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID


@Entity(tableName = "words")
data class WordEntity(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val originalText: String,
    val translatedText: String,
    val status: WordStatus,
    val sourceLanguage: String,
    val targetLanguage: String,
    val courseId: UUID
)


enum class WordStatus {
    New, Learning, GoodLearning, Known;

    fun inc(): WordStatus {
        return when (this) {
            New -> Learning
            Learning -> GoodLearning
            GoodLearning -> Known
            Known -> Known
        }
    }
}
