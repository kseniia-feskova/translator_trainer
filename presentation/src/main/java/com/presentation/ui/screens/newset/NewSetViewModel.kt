package com.presentation.ui.screens.newset

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.presentation.data.IDataStoreManager
import com.presentation.model.SetLevel
import com.presentation.model.SetOfCards
import com.presentation.model.WordUI
import com.presentation.usecases.IAddSetOfWordsUseCase
import com.presentation.usecases.IAddWordToSetUseCase
import com.presentation.usecases.IGetSetOfWordsUseCase
import com.presentation.usecases.IGetWordsOfSetUseCase
import com.presentation.utils.ALL_WORDS
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewSetViewModel(
    private val getAllWordsSet: IGetSetOfWordsUseCase,
    private val getAllWords: IGetWordsOfSetUseCase,
    private val addSetOfCards: IAddSetOfWordsUseCase,
    private val addWordToSet: IAddWordToSetUseCase,
    private val prefs: IDataStoreManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewSetUIState(loading = true))
    val uiState = _uiState.asStateFlow()

    private var allWords = mapOf<WordUI, Boolean>()

    init {
        viewModelScope.launch {
            if (allWords.isEmpty()) {
                val id = getAllWordsSet.invoke(ALL_WORDS)?.id
                if (id != null) {
                    getAllWords.invoke(id).collectLatest { list ->
                        allWords = list.toSelectingMap()
                        _uiState.update {
                            it.copy(words = allWords, loading = false)
                        }
                    }
                }
            }
        }
    }

    fun handleIntent(intent: NewSetIntent) {
        when (intent) {
            NewSetIntent.ClearSearch -> clearQuery()
            NewSetIntent.FilterClicked -> {}
            is NewSetIntent.SaveSet -> saveSet(intent.onSetSaved)
            is NewSetIntent.SelectWord -> selectWord(intent.word)
            is NewSetIntent.NameChange -> changeName(intent.name)
            is NewSetIntent.SaveCheckBoxChange -> updateCheckBox(intent.isSaveEnabled)
            is NewSetIntent.SearchWord -> searchQuery(intent.query)
        }
    }

    private fun saveSet(onSetSaved: () -> Unit) {
        _uiState.update { it.copy(loading = true) }
        viewModelScope.launch {
            val data = _uiState.value
            val set = SetOfCards(0, data.name, SetLevel.EASY, emptySet(), 0)
            val setId = addSetOfCards.invoke(set).toInt()

            data.words.filter { it.value }.keys.forEach {
                addWordToSet.invoke(setId = setId, wordId = it.id)
            }
            if (data.isSaveChecked) {
                prefs.saveSelectedSetId(setId)
            }
            _uiState.update { it.copy(loading = false) }
            onSetSaved()
        }

    }

    private fun changeName(name: String) {
        _uiState.update {
            it.copy(name = name)
        }
        checkActionButton()
    }

    private fun updateCheckBox(isEnabled: Boolean) {
        _uiState.update { it.copy(isSaveChecked = isEnabled) }
    }

    private fun selectWord(word: WordUI) {
        val updatedWords = _uiState.value.words.toMutableMap()
        val oldValue = updatedWords[word]
        if (oldValue != null) {
            val updatedAll = allWords.toMutableMap()
            updatedWords[word] = !oldValue
            updatedAll[word] = !oldValue
            allWords = updatedAll
            _uiState.update {
                it.copy(words = updatedWords)
            }
        }
        checkActionButton()
    }

    private fun searchQuery(query: String) {
        _uiState.update {
            it.copy(
                loading = true,
                query = query
            )
        }
        if (query.isNotEmpty()) {
            val filtered = searchByQuery(query)
            _uiState.update { it.copy(words = filtered, loading = false) }
        } else _uiState.update { it.copy(words = allWords, loading = false) }
    }


    private fun clearQuery() {
        _uiState.update {
            it.copy(loading = false, query = "", words = allWords)
        }
    }

    private fun searchByQuery(query: String): Map<WordUI, Boolean> {
        return allWords.filter { word ->
            word.key.originalText.contains(query, ignoreCase = true) || word.key.resText.contains(
                query,
                ignoreCase = true
            )
        }
    }

    private fun checkActionButton() {
        val isEnabled =
            _uiState.value.name.isNotEmpty() && allWords.filter { it.value }.isNotEmpty()
        _uiState.update {
            it.copy(isActionEnable = isEnabled)
        }
    }
}

sealed class NewSetIntent {
    data class NameChange(val name: String) : NewSetIntent()
    data class SaveCheckBoxChange(val isSaveEnabled: Boolean) : NewSetIntent()
    data class SearchWord(val query: String) : NewSetIntent()
    data class SelectWord(val word: WordUI) : NewSetIntent()
    object ClearSearch : NewSetIntent()
    object FilterClicked : NewSetIntent()
    data class SaveSet(val onSetSaved: () -> Unit) : NewSetIntent()
}
