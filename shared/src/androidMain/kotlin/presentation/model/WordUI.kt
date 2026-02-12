package presentation.model

// ui отображение WordEntity. Возможно понадобиться еще сохранять дату создания
data class WordUI(
    val id: String,
    val originalText: String,
    val resText: String,
    val level: Level,
  //  val date: Date = Date()
)

data class WordResult(
    val word: WordUI,
    val translation: String
)

data class WordViewData(
    val data: WordUI,
    val isOptionRevealed: Boolean = false
)

data class WordSelection(
    val id: String,
    val originalText: String,
    val resText: String,
    val isSaved: Boolean = false
)

fun WordUI.toSelection(): WordSelection {
    return WordSelection(
        id = id,
        originalText = originalText,
        resText = resText,
        isSaved = false
    )
}

