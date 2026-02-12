package presentation.model

import kotlinx.serialization.Serializable

@Serializable
enum class LessonType(val btnName: String) {
    BUBBLE("Bubble"), MATCH("Match"), DICTATION("Dictation")//, TRANSLATE("Translate"), CROSSWORD("Crossword")
}