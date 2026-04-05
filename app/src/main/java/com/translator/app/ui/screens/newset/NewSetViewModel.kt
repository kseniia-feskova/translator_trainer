package com.translator.app.ui.screens.newset

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import presentation.model.WordUI
import domain.usecases.course.ICoursesOnPrefsUseCases
import domain.usecases.sets.IAddSetUseCase
import domain.usecases.words.IGetWordsBySetUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mapper.toData
import mapper.toUI

class NewSetViewModel(
    private val getAllWords: IGetWordsBySetUseCase,
    private val saveSet: IAddSetUseCase,
    private val coursePrefs: ICoursesOnPrefsUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewSetUIState(loading = true))
    val uiState = _uiState.asStateFlow()

    private var allWords = mapOf<WordUI, Boolean>()
    private var isFiltered = false

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }
            if (allWords.isEmpty()) {
                val allWordsId = coursePrefs.getCourse()?.toUI()?.allWordsId
                Log.e("NewSet", "All words = $allWordsId")
                if (allWordsId != null) {
                    val response = getAllWords.invoke(allWordsId)
                    if (response.isSuccess) {
                        allWords = response.getOrNull()?.map { it.toUI() }?.toSelectingMap() ?: emptyMap()
                        _uiState.update { it.copy(words = allWords, loading = false) }
                    } else {
                        handleError(response)
                    }
                } else {
                    _uiState.update { it.copy(error = NewSetError.DEFAULT, loading = false) }
                }
            }
        }
    }

    fun handleIntent(intent: NewSetIntent) {
        when (intent) {
            NewSetIntent.ClearSearch -> clearQuery()
            NewSetIntent.FilterClicked -> {
                if (!isFiltered) {
                    _uiState.update {
                        it.copy(
                            words = allWords
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            words = getSortedWords()
                        )
                    }
                }
                isFiltered = !isFiltered
            }

            is NewSetIntent.SaveSet -> saveSet(intent.onSetSaved)
            is NewSetIntent.SelectWord -> selectWord(intent.word)
            is NewSetIntent.NameChange -> changeName(intent.name)
            is NewSetIntent.SaveCheckBoxChange -> updateCheckBox(intent.isSaveEnabled)
            is NewSetIntent.SearchWord -> searchQuery(intent.query)
            NewSetIntent.HideLimitsError -> _uiState.update { it.copy(limitsError = false) }
        }
    }

    fun getSortedWords(): Map<WordUI, Boolean> {
        return allWords
            .toList()
            .sortedWith(
                compareBy<Pair<WordUI, Boolean>> { it.first.level }
                    .thenBy { it.first.originalText }
            )
            .toMap()
    }

    private fun saveSet(onSetSaved: () -> Unit) {
        _uiState.update { it.copy(loading = true) }
        if (isActionBtnEnabled()) {
            viewModelScope.launch {
                val data = _uiState.value
                val wordsId = data.words.filter { it.value }.keys.map { it.id }
                val course = coursePrefs.getCourse()?.toUI()

                if (course != null) {
                    val response = saveSet.invoke(
                        name = data.name,
                        isDefault = data.isSaveChecked,
                        listOfWords = wordsId,
                        courseId = course.id
                    )
                    if (response.isSuccess) {
                        if (data.isSaveChecked) {
                            coursePrefs.saveOne(course.copy(selectedSetId = response.getOrNull()?.id.toString()).toData())
                        }
                        _uiState.update { it.copy(loading = false) }
                        onSetSaved()
                    } else {
                        handleError(response)
                    }
                } else {
                    _uiState.update { it.copy(loading = false, error = NewSetError.DEFAULT) }
                }
            }
        }
    }

    private fun changeName(name: String) {
        _uiState.update { it.copy(name = name, error = null) }
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
            val selected = updatedWords.filter { it.value }.size
            _uiState.update {
                it.copy(words = updatedWords, error = null, countOfSelected = selected)
            }
        }
    }

    private fun searchQuery(query: String) {
        _uiState.update {
            it.copy(
                loading = true,
                query = query,
                error = null
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

    private fun isActionBtnEnabled(): Boolean {
        val error = if (_uiState.value.name.isEmpty()) {
            NewSetError.EMPTY_FIELDS
        } else if (allWords.filter { it.value }.isEmpty()) {
            NewSetError.EMPTY_SELECTION
        } else null
        _uiState.update {
            it.copy(error = error, loading = false)
        }
        return error == null
    }

    private fun <T> handleError(response: Result<T>) {
        val error = response.exceptionOrNull()
        val errorMsg = when (error?.message) {
            "Failed to connect" -> NewSetError.INTERNET_CONNECTION_ERROR
            "Guest limit" -> {
                _uiState.update { it.copy(limitsError = true, loading = false) }
                null
            }

            else -> NewSetError.DEFAULT
        }
        if (errorMsg != null) {
            _uiState.update { it.copy(error = errorMsg, loading = false) }
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
    object HideLimitsError : NewSetIntent()
}
