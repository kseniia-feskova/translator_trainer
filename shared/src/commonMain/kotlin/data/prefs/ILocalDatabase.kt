package data.prefs

// common interface to interact with local database (room and sqlite3)
interface ILocalDatabase {
    fun clearDatabase()
}