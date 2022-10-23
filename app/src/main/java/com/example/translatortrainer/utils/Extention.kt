package com.example.translatortrainer.utils

import androidx.appcompat.widget.SearchView
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


fun SearchView.getQueryTextChangeStateFlow(): StateFlow<String> {
    val serachQuery = MutableStateFlow("Geeks")
    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            serachQuery.value = newText ?: ""
            return true
        }
    })
    return serachQuery
}