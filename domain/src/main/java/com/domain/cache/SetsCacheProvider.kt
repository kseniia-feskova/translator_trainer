package com.domain.cache

import com.presentation.cache.ISetsCacheProvider
import com.presentation.model.SetOfCards
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class SetsCacheProvider : ISetsCacheProvider {

    private val _sets = MutableStateFlow<List<SetOfCards>?>(null)

    override fun addSets(newList: List<SetOfCards>?) {
        _sets.update { newList }
    }

    override fun getSets(): List<SetOfCards>? {
        return _sets.value
    }

}