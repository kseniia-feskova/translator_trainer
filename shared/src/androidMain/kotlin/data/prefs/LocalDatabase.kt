package data.prefs

import data.room.AppDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class LocalDatabase(val dao: AppDao, val scope: CoroutineScope) : ILocalDatabase {

    override fun clearDatabase() {
        scope.launch {
            dao.clearUsers()
            dao.clearWords()
            dao.clearSets()
        }
    }

}