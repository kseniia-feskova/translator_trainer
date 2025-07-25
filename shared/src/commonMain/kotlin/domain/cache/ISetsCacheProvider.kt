package domain.cache

import data.model.sets.SetResponse

interface ISetsCacheProvider {
    fun addSets(newList: List<SetResponse>?)

    fun getSets(): List<SetResponse>?

    fun clear()
}