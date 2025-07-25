package domain.cache

import data.model.sets.SetResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class SetsCacheProvider : ISetsCacheProvider {

    private val _sets = MutableStateFlow<List<SetResponse>?>(null)

    override fun addSets(newList: List<SetResponse>?) {
        _sets.update { newList }
    }

    override fun getSets(): List<SetResponse>? {
        return _sets.value
    }

    override fun clear() {
        _sets.update { null }
    }

}