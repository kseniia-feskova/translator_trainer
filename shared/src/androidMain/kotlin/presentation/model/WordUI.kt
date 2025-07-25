package presentation.model

// ui отображение WordEntity. Возможно понадобиться еще сохранять дату создания
data class WordUI(
    val id: String,
    val originalText: String,
    val resText: String,
    val level: Level,
  //  val date: Date = Date()
)


