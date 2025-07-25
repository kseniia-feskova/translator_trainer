package data.model.words

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