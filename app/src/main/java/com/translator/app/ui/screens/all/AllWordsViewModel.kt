package com.translator.app.ui.screens.all

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import presentation.model.WordUI
import presentation.model.WordViewData
import domain.usecases.words.IDeleteWordUseCase
import domain.usecases.words.IGetWordsBySetUseCase
import mapper.toUI

class AllWordsViewModel(
    savedStateHandle: SavedStateHandle,
    getWords: IGetWordsBySetUseCase,
    private val deleteWord: IDeleteWordUseCase
) : ViewModel() {
    private val setId = savedStateHandle.setId
    private val queryInput = MutableStateFlow("")

    private val setUiFlow = getWords.invokeFlow(setId)
        .map { result ->
            Log.e("AllWordsVM", "setUiFlow, result = $result")
            val words = result.getOrNull()?.map { it.toUI() }
            if (result.isSuccess && words != null) {
                words
            } else {
                emptyList<WordUI>()
            }
        }
        .catch {
            //   emit(AllWordsUIState(error = it.message))
        }

    val uiState = combine(
        setUiFlow, queryInput
    ) { setUiFlow, queryInput ->
        if (queryInput.isNotEmpty()) {
            val filtered = searchByQuery(setUiFlow, queryInput)
            AllWordsUIState(
                query = queryInput,
                words = filtered.map { WordViewData(it, false) },
                loading = false
            )

        } else {
            AllWordsUIState(
                query = queryInput,
                words = setUiFlow.map { WordViewData(it, false) },
                loading = false
            )
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        AllWordsUIState(loading = true)
    )

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
        queryInput.update { query }
    }

    private fun revealOptions(word: WordViewData) {
//        _uiState.update { state ->
//            state.copy(
//                words = state.words.map { wordInList ->
//                    if (wordInList.data == word.data) {
//                        wordInList.copy(isOptionRevealed = true)
//                    } else wordInList
//                }
//            )
//        }
    }

    private fun collapseOptions(word: WordViewData) {
//        _uiState.update { state ->
//            state.copy(
//                words = state.words.map { wordInList ->
//                    if (wordInList.data == word.data) {
//                        wordInList.copy(isOptionRevealed = false)
//                    } else wordInList
//                }
//            )
//        }
    }

    private fun clearQuery() {
        queryInput.update { "" }
    }

    private fun deleteWord(word: WordUI) {
        viewModelScope.launch {
            val deletion = deleteWord.invoke(word.id)
            if (deletion.isSuccess) {
                Log.e("AllWordsViewModel", "The word $word is deleted")
            }
        }
    }

    private fun searchByQuery(list: List<WordUI>, query: String): List<WordUI> {
        return list.filter { word ->
            word.originalText.contains(query, ignoreCase = true) || word.resText.contains(
                query,
                ignoreCase = true
            )
        }
    }
}