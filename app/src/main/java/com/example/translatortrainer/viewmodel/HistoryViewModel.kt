package com.example.translatortrainer.viewmodel

import androidx.appcompat.widget.SearchView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.translatortrainer.data.MainRepository
import com.example.translatortrainer.data.WordEntity
import com.example.translatortrainer.room.HistoryDAO
import com.example.translatortrainer.utils.ConstTest
import com.example.translatortrainer.utils.CustomTranslator
import com.example.translatortrainer.utils.Language
import com.example.translatortrainer.utils.getQueryTextChangeStateFlow
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryViewModel(private val repository: MainRepository) : ViewModel() {

    private val _allWords = MutableLiveData(ConstTest.getList())
    val allWords: LiveData<List<WordEntity>> = _allWords

    fun startObserve() {
        repository.getAllObservable().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).map { _allWords.value = it }.subscribe()
    }

    private val _word = MutableSharedFlow<String>()
    val word: SharedFlow<String> = _word.asSharedFlow()

    private val _translation = MutableSharedFlow<String>()
    val translation: SharedFlow<String> = _translation.asSharedFlow()

    fun getSearch(searchView: SearchView) {
        viewModelScope.launch {
            searchView.getQueryTextChangeStateFlow()
                .debounce(300)
                .filter { query ->
                    if (query.isEmpty()) {
                        _translation.tryEmit("")
                        return@filter false
                    } else {
                        return@filter true
                    }
                }
                .distinctUntilChanged()
                .flowOn(Dispatchers.Default)
                .collect { result ->
                    _word.tryEmit(result)
                }
        }
    }

    fun translate(text: String) {
        viewModelScope.launch {
            val translation = repository.getTranslate(text, Language.GERMAN)
            _translation.emit(translation)
            addWord(text,translation)
        }
    }
    private fun addWord(text:String, translation:String){
        viewModelScope.launch {
            repository.addNewWord(WordEntity(3, text, translation, "German"))
        }
    }
}