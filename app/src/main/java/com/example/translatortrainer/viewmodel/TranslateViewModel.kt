package com.example.translatortrainer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.repository.translate.ITranslateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class TranslateViewModel(
    private val translateRepository: ITranslateRepository
) : ViewModel() {

    private val _translation = MutableSharedFlow<String>(replay = 1)
    val translation: SharedFlow<String> = _translation.asSharedFlow()

    fun translate (text: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val translate = translateRepository.getTranslate(text)
                println("Перевод = $translate")
                _translation.emit(translate?:"error")
            } catch (e: Exception) {
                println("Исключение: ${e.message}")
            }
        }
    }
}