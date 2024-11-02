package com.example.translatortrainer.viewmodel.courses

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.translatortrainer.test.listOfDummyCards
import com.example.translatortrainer.test.model.WordUI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SelectTranslateViewModel() : ViewModel() {

    private val allWords = listOfDummyCards.toMutableSet()
    private val _index = MutableStateFlow(0)
    val index = _index.asStateFlow()

    private val _uiState = MutableStateFlow(
        CourseData.SelectTranslationData(
            currentWord = allWords.elementAt(0),
            nextWord = allWords.elementAt(1),
            translations = allWords.map { it.resText }.shuffled(),
            currentWordIndex = 1,
            allWordsCount = allWords.size
        )
    )
    val uiState = _uiState.asStateFlow()


    fun handleIntent(intent: SelectTranslationIntent) {
        when (intent) {
            is SelectTranslationIntent.SelectTranslation -> {
                checkSelectedWord(intent.selected)
            }

            is SelectTranslationIntent.DoNotKnow -> {
                doNotKnowSelected()
            }
        }
    }

    private fun checkSelectedWord(selected: String) {
        val word = _uiState.value.currentWord
        if (selected == word.resText) {
            handleSuccessSelect()
        } else {
            //можно ничего не делать. только UI будет отображаться
        }
    }

    private fun doNotKnowSelected() {
        val word = _uiState.value.currentWord //берём текущее слово
        allWords.remove(word) //убираем его из набора для изучения
        val list = allWords.toMutableList() // создаем список без текущего слова
        list.add(word) //добавляем слово в конец
        allWords.clear() //обновляем набор слов из списка
        allWords.addAll(list)
        nextWord() //переходим к следующему слову
    }

    private fun handleSuccessSelect() {
        _index.update { it.inc() } //в верхнем меню двигаем ползунок с результатом
        allWords.remove(_uiState.value.currentWord) //убираем из сета неизученных слов, слово, которое нашли
        nextWord()
    }

    private fun nextWord() {
        //проверка на то, есть ли еще слова в allWords
        if (allWords.isNotEmpty()) {
            //Берем первое слово из списка, перемешиваем список с переводами
            _uiState.update {
                it.copy(
                    currentWord = allWords.elementAt(0),
                    nextWord = allWords.elementAtOrNull(1),
                    translations = listOfDummyCards.map { it.resText }.shuffled(),
                    currentWordIndex = _index.value,
                )
            }
        } else {
            // если список для изучения пустой, то можем заканчивать уровень
            finishLevel()
        }
    }

    private fun finishLevel() {
        // можно завершать уровень и приподнимать словам уровень по знанию
        Log.e("SelectTranslateViewModel", "Уровень пройден")
    }

}

sealed class CourseData(
    open val currentWordIndex: Int,
    open val allWordsCount: Int = 5
) {

    data class SelectTranslationData(
        val currentWord: WordUI,
        val nextWord: WordUI?,
        val translations: List<String>,
        val selectedOption: String = "",
        override val currentWordIndex: Int,
        override val allWordsCount: Int = 5
    ) : CourseData(currentWordIndex, allWordsCount)
}

sealed class SelectTranslationIntent {
    data class SelectTranslation(val selected: String) : SelectTranslationIntent()
    object DoNotKnow : SelectTranslationIntent()
}