package com.example.translatortrainer.viewmodel

import androidx.appcompat.widget.SearchView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.translatortrainer.data.MainRepository
import com.example.translatortrainer.data.WordEntity
import com.example.translatortrainer.utils.ConstTest
import com.example.translatortrainer.utils.Language
import com.example.translatortrainer.utils.getQueryTextChangeStateFlow
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent

class TranslationViewModel(private val repository: MainRepository) : ViewModel(), KoinComponent {

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
            _translation.tryEmit(translation)
        }
    }


}