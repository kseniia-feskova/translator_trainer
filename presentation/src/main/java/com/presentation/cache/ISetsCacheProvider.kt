package com.presentation.cache

import com.presentation.model.SetOfCards

interface ISetsCacheProvider {
    fun addSets(newList: List<SetOfCards>?)

    fun getSets(): List<SetOfCards>?
}