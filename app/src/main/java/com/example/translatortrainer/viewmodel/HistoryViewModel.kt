package com.example.translatortrainer.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.data.WordEntity
import com.data.data.WordsRepository
import com.data.utils.Language
import io.ktor.util.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class HistoryViewModel(private val repository: WordsRepository) : ViewModel() {

    private val _allWords = MutableLiveData(emptyList<WordEntity>())
    val allWords: LiveData<List<WordEntity>> = _allWords

    private val _translation = MutableSharedFlow<String>(replay = 1)
    val translation: SharedFlow<String> = _translation.asSharedFlow()

    private var _language = Language.GERMAN
    fun startHistoryObserve() {
        repository.getLastWord(5).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { _allWords.value = it.reversed() }
            .subscribe()
    }

    fun translate(text: String) {
        viewModelScope.launch {
            try {
                val translation = repository.getTranslate(text, _language)
                _translation.emit(translation)
                addWord(text, translation, _language.name)
            } catch (e: Exception) {
                Log.e("translate", "Error = ${e.message}")
                _translation.emit("Error")
            }
        }
    }

    fun setLanguage(language: String) {
        when (language) {
            Language.GERMAN.name.toLowerCasePreservingASCIIRules() -> _language = Language.GERMAN
            Language.FRENCH.name.toLowerCasePreservingASCIIRules() -> _language = Language.FRENCH
            Language.ENGLISH.name.toLowerCasePreservingASCIIRules() -> _language = Language.ENGLISH
        }
    }

    private fun addWord(text: String, translation: String, language: String) {
        viewModelScope.launch {
            repository.addNewWord(WordEntity(3, text, translation, language))
        }
    }
}