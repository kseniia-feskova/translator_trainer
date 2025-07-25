package mapper

import presentation.model.Level
import data.model.words.WordStatus

fun Level.toStatus(): WordStatus {
    return when (this) {
        Level.NEW -> WordStatus.New
        Level.LEARNING_GOOD -> WordStatus.GoodLearning
        Level.LEARNING -> WordStatus.Learning
        Level.KNOW -> WordStatus.Known
    }
}

fun WordStatus.toLevel(): Level {
    return when (this) {
        WordStatus.New -> Level.NEW
        WordStatus.GoodLearning -> Level.LEARNING_GOOD
        WordStatus.Learning -> Level.LEARNING
        WordStatus.Known -> Level.KNOW
    }
}