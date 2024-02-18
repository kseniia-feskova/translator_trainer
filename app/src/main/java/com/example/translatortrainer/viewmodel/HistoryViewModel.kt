package com.example.translatortrainer.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.translatortrainer.data.MainRepository
import com.example.translatortrainer.data.WordEntity
import com.example.translatortrainer.utils.ConstTest
import com.example.translatortrainer.utils.Language
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class HistoryViewModel(private val repository: MainRepository) : ViewModel() {

    private val _allWords = MutableLiveData(ConstTest.getList())
    val allWords: LiveData<List<WordEntity>> = _allWords

    private val _translation = MutableSharedFlow<String>()
    val translation: SharedFlow<String> = _translation.asSharedFlow()

    fun startObserve() {
        repository.getLastWord(5).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { _allWords.value = it }
            .subscribe()
    }

    fun translate(text: String) {
        viewModelScope.launch {
            try {
                val translation = repository.getTranslate(text, Language.GERMAN)
                _translation.emit(translation)
                addWord(text, translation)
            } catch (e: Exception) {
                Log.e("translate", "Error = ${e.message}")
                _translation.emit("Error")
            }
        }
    }

    private fun addWord(text: String, translation: String) {
        viewModelScope.launch {
            repository.addNewWord(WordEntity(3, text, translation, "German"))
        }
    }
}