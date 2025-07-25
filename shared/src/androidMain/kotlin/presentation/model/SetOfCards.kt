package presentation.model

data class SetOfCards(
    val id: String,
    val title: String,
    val isDefault: Boolean,
    val words: List<WordUI>,
)
