package com.presentation.ui.screens.all

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.presentation.model.WordUI
import com.presentation.ui.screens.set.setId
import com.presentation.usecases.words.IDeleteWordUseCase
import com.presentation.usecases.words.IGetWordsOfSetUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AllWordsViewModel(
    savedStateHandle: SavedStateHandle,
    private val getAllWords: IGetWordsOfSetUseCase,
    private val deleteWord: IDeleteWordUseCase
) : ViewModel() {

    private val setId = savedStateHandle.setId
    private val allWords = mutableListOf<WordUI>()

    private val _uiState = MutableStateFlow(AllWordsUIState(loading = true))
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getAllWords.invoke(setId).collectLatest { list ->
                allWords.clear()
                allWords.addAll(list.sortedBy { it.level })
                _uiState.update {
                    it.copy(words = allWords, loading = false)
                }
            }
        }
    }

    fun handleIntent(intent: AllWordsIntent) {
        when (intent) {
            AllWordsIntent.ClearSearch -> clearQuery()
            AllWordsIntent.CloseDialog -> closeWordMenu()
            is AllWordsIntent.Delete -> deleteWord(intent.word)
            is AllWordsIntent.Search -> searchQuery(intent.query)
            is AllWordsIntent.WordSelected -> selectWord(intent.selected, intent.dialogOffset)
            is AllWordsIntent.Edit -> {
                //   navigateWithWordID intent.word.id
            }

            AllWordsIntent.Filter -> {
                //BottomSheetWithSelect
            }
        }
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

    private fun closeWordMenu() {
        _uiState.update {
            it.copy(selectedItem = null, popupOffset = null)
        }
    }

    private fun deleteWord(word: WordUI) {
        _uiState.update { it.copy(loading = true) }
        viewModelScope.launch { deleteWord.invoke(word.id)  }
    }

    private fun selectWord(word: WordUI?, offset: Offset?) {
        _uiState.update {
            it.copy(
                selectedItem = word,
                popupOffset = offset
            )
        }
    }

    private fun searchByQuery(query: String): List<WordUI> {
        return allWords.filter { word ->
            word.originalText.contains(query, ignoreCase = true) || word.resText.contains(query, ignoreCase = true)
        }
    }
}