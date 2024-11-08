package com.example.translatortrainer.viewmodel.courses

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.model.WordStatus
import com.domain.usecase.IGetSetOfCardsUseCase
import com.domain.usecase.IGetWordsOfSetUseCase
import com.domain.usecase.IUpdateWordUseCase
import com.example.translatortrainer.mapper.toWordUI
import com.example.translatortrainer.test.model.Level
import com.example.translatortrainer.test.model.WordUI
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SelectTranslateViewModel(
    private val setId: Int,
    private val getWords: IGetWordsOfSetUseCase,
    private val getSet: IGetSetOfCardsUseCase,
    private val updateWord: IUpdateWordUseCase
) : ViewModel() {

    private var allWords = setOf<WordUI>()//listOfDummyCards.toMutableSet()
    private var translations = setOf<String>()
    private var setName: String = ""
    private val _index = MutableStateFlow(0)

    private val _uiState =
        MutableStateFlow(CourseData.SelectTranslationData())
    val uiState = _uiState.asStateFlow()

    // у меня есть айди набора
// я по нему беру набор (айди, название и спислок слов)
    // из списка слов беру пять неизвестных и создаю allWords
    init {
        viewModelScope.launch {
            setName = getSet.invoke(setId)?.name ?: ""
            getWords.invoke(setId).collect { words ->
                if (translations.isEmpty()) {
                    val firstFive = words.filter { it.status != WordStatus.LEARNED }
                    allWords = if (firstFive.size > 5) {
                        firstFive.take(5).map { it.toWordUI() }.toSet()
                    } else firstFive.map { it.toWordUI() }.toSet()
                    translations = allWords.map { it.resText }.toSet()
                    nextWord()
                }
            }
        }
    }

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
        viewModelScope.launch {
            _uiState.update { it.copy(selectedOption = selected) }
            delay(1000)
            val word = _uiState.value.currentWord
            if (selected == word.resText) {
                handleSuccessSelect()
            } else {
                //можно ничего не делать. только UI будет отображаться
            }
        }
    }

    private fun doNotKnowSelected() {
        val word = _uiState.value.currentWord //берём текущее слово
        //   allWords.remove(word) //убираем его из набора для изучения
        val list = allWords.toMutableList() // создаем список без текущего слова
        list.remove(word)
        list.add(word) //добавляем слово в конец
        allWords = list.toSet()//обновляем набор слов из списка
        nextWord() //переходим к следующему слову
    }

    private fun handleSuccessSelect() {
        _index.update { it.inc() } //в верхнем меню двигаем ползунок с результатом
        val list = allWords.toMutableList() // создаем список без текущего слова
        list.remove(_uiState.value.currentWord)
        allWords = list.toSet()//обновляем набор слов из списка
        nextWord()
    }

    private fun nextWord() {
        //проверка на то, есть ли еще слова в allWords
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


    private fun finishLevel() {
        // можно завершать уровень и приподнимать словам уровень по знанию
        Log.e("SelectTranslateViewModel", "Уровень пройден")
        viewModelScope.launch {
            launch {
                getWords.invoke(setId).collect { words ->
                    val firstFive = words.filter { translations.contains(it.translation) }
                    firstFive.forEach {
                        updateWord.invoke(it.copy(status = it.status.inc()))
                    }
                }
            }
            _uiState.update {
                it.copy(
                    finish = true
                )
            }
        }
    }

}

sealed class CourseData(
    open val currentWordIndex: Int,
    open val allWordsCount: Int = 5,
    open val setName: String = ""
) {

    data class SelectTranslationData(
        val currentWord: WordUI = WordUI(0, "", "", Level.LEARNING),
        val nextWord: WordUI? = null,
        val translations: List<String> = emptyList(),
        val selectedOption: String = "",
        val finish: Boolean = false,
        override val currentWordIndex: Int = 0,
        override val allWordsCount: Int = 5,
        override val setName: String = ""
    ) : CourseData(currentWordIndex, allWordsCount, setName)
}

sealed class SelectTranslationIntent {
    data class SelectTranslation(val selected: String) : SelectTranslationIntent()
    object DoNotKnow : SelectTranslationIntent()
}