package domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class LessonType {
    BUBBLE, TRANSLATE, CROSSWORD
}