package presentation.ui.screens.all

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import presentation.model.WordUI
import presentation.model.WordViewData
import presentation.usecases.words.IDeleteWordUseCase
import presentation.usecases.words.IGetWordsBySetUseCase

class AllWordsViewModel(
    savedStateHandle: SavedStateHandle,
    private val getWords: IGetWordsBySetUseCase,
    private val deleteWord: IDeleteWordUseCase
) : ViewModel() {

    private val setId = savedStateHandle.setId
    private val allWords = mutableListOf<WordUI>()

    private val _uiState = MutableStateFlow(AllWordsUIState(loading = true))
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val response = getWords.invoke(setId)
            if (response.isSuccess) {
                val words = response.getOrNull() ?: emptyList()
                allWords.clear()
                allWords.addAll(words.sortedBy { it.level })
                _uiState.update {
                    it.copy(words = allWords.map { WordViewData(it, false) }, loading = false)
                }
            } else {
                //TODO: handle error case
            }
        }
    }

    fun handleIntent(intent: AllWordsIntent) {
        when (intent) {
            AllWordsIntent.ClearSearch -> clearQuery()
            is AllWordsIntent.Delete -> deleteWord(intent.word)
            is AllWordsIntent.Search -> searchQuery(intent.query)
            is AllWordsIntent.OnActionsRevealed -> revealOptions(intent.word)
            is AllWordsIntent.OnCollapsed -> collapseOptions(intent.word)
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
            _uiState.update {
                it.copy(
                    words = filtered.map { WordViewData(it, false) },
                    loading = false
                )
            }
        } else _uiState.update {
            it.copy(
                words = allWords.map { WordViewData(it, false) },
                loading = false
            )
        }
    }

    private fun revealOptions(word: WordViewData) {
        _uiState.update { state ->
            state.copy(
                words = state.words.map { wordInList ->
                    if (wordInList.data == word.data) {
                        wordInList.copy(isOptionRevealed = true)
                    } else wordInList
                }
            )
        }
    }

    private fun collapseOptions(word: WordViewData) {
        _uiState.update { state ->
            state.copy(
                words = state.words.map { wordInList ->
                    if (wordInList.data == word.data) {
                        wordInList.copy(isOptionRevealed = false)
                    } else wordInList
                }
            )
        }
    }

    private fun clearQuery() {
        _uiState.update {
            it.copy(loading = false, query = "", words = allWords.map { WordViewData(it, false) })
        }
    }

    private fun deleteWord(word: WordUI) {
        viewModelScope.launch {
            val deletion = deleteWord.invoke(word.id)
            if (deletion.isSuccess) {
                val response = getWords.invoke(setId)
                if (response.isSuccess) {
                    val words = response.getOrNull() ?: emptyList()
                    allWords.clear()
                    allWords.addAll(words.sortedBy { it.level })
                    _uiState.update {
                        it.copy(words = allWords.map { WordViewData(it, false) }, loading = false)
                    }
                }
            }
        }
    }

    private fun searchByQuery(query: String): List<WordUI> {
        return allWords.filter { word ->
            word.originalText.contains(query, ignoreCase = true) || word.resText.contains(
                query,
                ignoreCase = true
            )
        }
    }
}