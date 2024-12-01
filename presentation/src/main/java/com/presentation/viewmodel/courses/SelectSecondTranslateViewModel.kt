package com.presentation.viewmodel.courses

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.presentation.model.Level
import com.presentation.model.WordUI
import com.presentation.usecases.IGetSetOfCardsUseCase
import com.presentation.usecases.IGetWordsOfSetUseCase
import com.presentation.usecases.IUpdateWordUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SelectSecondTranslateViewModel(
    private val setId: Int,
    private val getWords: IGetWordsOfSetUseCase,
    private val getSet: IGetSetOfCardsUseCase,
    private val updateWord: IUpdateWordUseCase
) : ViewModel() {

    private var allWords: Set<WordUI>? = null
    private var translations = setOf<String>()
    private var setName: String = ""
    private val _index = MutableStateFlow(0)

    private val _uiState =
        MutableStateFlow(CourseData.SelectSecondTranslationData())
    val uiState = _uiState.asStateFlow()

    // у меня есть айди набора
// я по нему беру набор (айди, название и спислок слов)
    // из списка слов беру пять неизвестных и создаю allWords
    init {
        Log.d(TAG, "SetId = $setId")
        viewModelScope.launch {
            setName = getSet.invoke(setId)?.title ?: ""
            val words = getWords.invoke(setId).first()
            if (translations.isEmpty()) {
                allWords = words.toSet()
                translations = allWords?.map { it.originalText }?.toSet() ?: setOf()
                Log.e(TAG, "Words = $allWords, \ntranslations = $translations")
                allWords?.let { allWords ->
                    _uiState.update {
                        it.copy(
                            setName = setName,
                            allWordsCount = translations.size,
                            currentWord = allWords.elementAt(0),
                            nextWord = allWords.elementAtOrNull(1),
                            translations = translations.shuffled(),
                            currentWordIndex = _index.value,
                            selectedOption = ""
                        )
                    }
                }
            }
        }
    }

    fun handleIntent(intent: SelectSecondTranslationIntent) {
        when (intent) {
            is SelectSecondTranslationIntent.SelectTranslation -> {
                Log.e("Select", "State = ${_uiState.value}")
                checkSelectedWord(intent.selected)
            }

            is SelectSecondTranslationIntent.DoNotKnow -> {
                doNotKnowSelected()
            }
        }
    }

    private fun checkSelectedWord(selected: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(selectedOption = selected) }
            delay(1000)
            val word = _uiState.value.currentWord
            if (selected == word.originalText) {
                handleSuccessSelect()
            } else {
                //можно ничего не делать. только UI будет отображаться
            }
        }
    }

    private fun doNotKnowSelected() {
        val word = _uiState.value.currentWord //берём текущее слово
        //   allWords.remove(word) //убираем его из набора для изучения
        val list = allWords?.toMutableList() ?: mutableListOf() // создаем список без текущего слова
        list.remove(word)
        list.add(word) //добавляем слово в конец
        allWords = list.toSet()//обновляем набор слов из списка
        nextWord() //переходим к следующему слову
    }

    private fun handleSuccessSelect() {
        _index.update { it.inc() } //в верхнем меню двигаем ползунок с результатом
        val list = allWords?.toMutableList() ?: mutableListOf() // создаем список без текущего слова
        list.remove(_uiState.value.currentWord)
        allWords = list.toSet()//обновляем набор слов из списка
        nextWord()
    }

    private fun nextWord() {
        //проверка на то, есть ли еще слова в allWords
        allWords?.let { allWords ->
            if (allWords.isNotEmpty()) {
                //Берем первое слово из списка, перемешиваем список с переводами
                _uiState.update {
                    it.copy(
                        setName = setName,
                        allWordsCount = translations.size,
                        currentWord = allWords.elementAt(0),
                        nextWord = allWords.elementAtOrNull(1),
                        translations = translations.shuffled(),
                        currentWordIndex = _index.value,
                        selectedOption = ""
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        nextWord = null,
                        translations = translations.shuffled(),
                        currentWordIndex = _index.value,
                        selectedOption = "",
                    )
                }
                // если список для изучения пустой, то можем заканчивать уровень
                finishLevel()
            }
        }
    }


    private fun finishLevel() {
        // можно завершать уровень и приподнимать словам уровень по знанию
        Log.e("SelectTranslateViewModel", "Уровень пройден")
        viewModelScope.launch {
            val firstFive =
                getWords.invoke(setId).first().filter { translations.contains(it.resText) }
            firstFive.forEach {
                updateWord.invoke(it.copy(level = Level.LEARNING_GOOD))
            }
            _uiState.update {
                it.copy(
                    finish = true
                )
            }
        }
    }

    companion object {
        private const val TAG = "SelectSecondTranslateViewModel"
    }

}


sealed class SelectSecondTranslationIntent {
    data class SelectTranslation(val selected: String) : SelectSecondTranslationIntent()
    object DoNotKnow : SelectSecondTranslationIntent()
}