package presentation.cache

import presentation.model.WordResult

interface IUiDataManager {

    fun saveWordsTranslation(wordsResult: List<WordResult>)

    fun getWordsTranslation(): List<WordResult>

}

class UiDataManager : IUiDataManager {
    private val wordsResult = mutableListOf<WordResult>()

    override fun saveWordsTranslation(wordsResult: List<WordResult>) {
        this.wordsResult.clear()
        this.wordsResult.addAll(wordsResult)
    }

    override fun getWordsTranslation(): List<WordResult> = wordsResult

}