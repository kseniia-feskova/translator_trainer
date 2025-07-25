package mapper

import presentation.model.Level
import presentation.model.WordUI
import data.model.words.WordEntity
import data.model.words.WordResponse
import java.util.UUID

fun WordEntity.toWord(): WordUI {
    return WordUI(
        id = id,
        originalText = originalText,
        resText = translatedText,
        level = status.toLevel(),
//        date = dateAdded
    )
}

fun WordResponse.toDao(): WordUI {
    return WordUI(
        id = UUID.randomUUID().toString(),
        originalText = originalText,
        resText = translatedText,
        level = Level.NEW
    )
}


fun WordResponse.toUI(): WordUI {
    return WordUI(
        id = id,
        resText = translatedText,
        originalText = originalText,
        level = status.toLevel()
    )
}