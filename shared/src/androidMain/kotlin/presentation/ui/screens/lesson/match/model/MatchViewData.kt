package presentation.ui.screens.lesson.match.model

sealed class MatchViewData(open val id: String) {
    data class MatchWordData(
        override val id: String,
        val text: String,
        val isSelected: Boolean = false,
        val isMatched: Boolean = false
    ) : MatchViewData(id)

    data class MatchEmpty(
        override val id: String
    ) : MatchViewData(id)
}