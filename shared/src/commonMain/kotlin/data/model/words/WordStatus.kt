package data.model.words

import kotlinx.serialization.Serializable

@Serializable
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