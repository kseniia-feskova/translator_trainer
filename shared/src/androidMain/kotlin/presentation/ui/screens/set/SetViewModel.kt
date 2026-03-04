package com.presentation.ui.screens.set

import android.content.Context
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import presentation.model.Level
import presentation.model.WordUI
import presentation.ui.screens.auth.BaseError
import presentation.ui.screens.set.CardSetIntent
import presentation.ui.screens.set.SetUIEvent
import presentation.ui.screens.set.SetUIState
import presentation.ui.screens.set.setId
import presentation.ui.screens.set.setName
import presentation.usecases.words.IGetWordsBySetUseCase
import presentation.usecases.words.IUpdateStatusUseCase

class SetViewModel(
    savedStateHandle: SavedStateHandle,
    private val getWords: IGetWordsBySetUseCase,
    private val updateWord: IUpdateStatusUseCase
) : ViewModel() {

    private val setId = savedStateHandle.setId
    private val setName: String = savedStateHandle.setName

    private val _uiState = MutableStateFlow(SetUIState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<SetUIEvent>()
    val events = _events.asSharedFlow()

    private val allWords: MutableList<WordUI> = mutableListOf()
    private val knownWords: MutableList<WordUI> = mutableListOf()
    private var index = 0

    fun getSetId() = setId

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            val response = getWords.invoke(setId)
            if (response.isSuccess) {
                val words = response.getOrNull() ?: emptyList()
                allWords.addAll(words)
                knownWords.addAll(words.filter { it.level == Level.KNOW })
                _uiState.update {
                    it.copy(
                        name = setName,
                        allWords = allWords.size
                    )
                }
                updateUI()
            } else {
                handleError(response)
            }
        }
    }

    fun handleIntent(intent: CardSetIntent) {
        when (intent) {
            is CardSetIntent.AddWordToKnow -> addWordToKnow(intent.word)
            is CardSetIntent.AddWordToLearn -> addWordToLearn(intent.word)
            is CardSetIntent.ResetCardSet -> resetCardSet()
            is CardSetIntent.CourseSelection -> _uiState.update { it.copy(selectLessonVisible = intent.isVisible) }
            is CardSetIntent.DownloadCardSet -> {
                viewModelScope.launch { _events.emit(SetUIEvent.RequestExport) }
            }
            is CardSetIntent.ExportFile -> {
                downloadCardSet(intent.uri, intent.context)
            }
        }
    }

    private fun resetCardSet() {
        index = 0
        allWords.clear()
        knownWords.clear()
        loadData()
    }

    private fun addWordToKnow(word: WordUI) {
        viewModelScope.launch {
            val result = updateWord.invoke(word.id, level = Level.KNOW)
            if (result.isSuccess) {
                result.getOrNull()?.let {
                    if (!knownWords.contains(it)) {
                        knownWords.add(it)
                    }
                    index++
                    updateUI()
                }
            } else {
                handleError(result)
            }
        }
    }


    private fun addWordToLearn(word: WordUI) {
        viewModelScope.launch {
            val result = updateWord.invoke(word.id, level = Level.NEW)
            if (result.isSuccess) {
                result.getOrNull()?.let { word ->
                    knownWords.removeIf { it.id == word.id }
                    index++
                    updateUI()
                }
            } else {
                handleError(result)
            }
        }
    }

    private fun downloadCardSet(uri: Uri, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            context.contentResolver.openOutputStream(uri)?.use { output ->
                val csv = buildCsv()
                output.write(csv.toByteArray())
            }
        }
    }

    private fun buildCsv(): String {
        val header = listOf("Original", "Translate", "Level")

        return buildString {
            appendLine(header.joinToString(","))

            allWords.forEach { word ->
                val row = listOf(
                    word.originalText.escapeCsv(),
                    word.resText.escapeCsv(),
                    word.level.name
                )
                appendLine(row.joinToString(","))
            }
        }
    }

    private fun String.escapeCsv(): String {
        var value = this

        if (value.contains("\"")) {
            value = value.replace("\"", "\"\"")
        }

        return if (value.contains(",") || value.contains("\n") || value.contains("\"")) {
            "\"$value\""
        } else {
            value
        }
    }

    private fun updateUI() {
        val first = allWords.getOrNull(index)
        val second = allWords.getOrNull(index + 1)
        _uiState.update {
            it.copy(
                words = if (first != null) Pair(first, second) else null,
                knowWords = knownWords.size
            )
        }
    }

    private fun <T> handleError(response: Result<T>) {
        val error = response.exceptionOrNull()
        val errorMsg = when (error?.message) {
            "Failed to connect" -> BaseError.INTERNET_CONNECTION_ERROR
            else -> BaseError.DEFAULT
        }
        _uiState.update { it.copy(error = errorMsg, loading = false) }
    }
}

