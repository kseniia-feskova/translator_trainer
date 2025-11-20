package presentation.model

import kotlinx.serialization.Serializable

@Serializable
enum class LessonType(val btnName: String) {
    BUBBLE ("Bubble"), TRANSLATE("Translate"), CROSSWORD ("Crossword")
}