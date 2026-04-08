package data.prefs

// It is needed for the flow wrapping
//abstract class BaseDataStoreManager: IDataStoreManager {
//    protected val userIdFlow = MutableStateFlow<String?>(null)
//
//    override fun listenUserId(): Flow<String?> = userIdFlow.asStateFlow()
//
//    override suspend fun saveUserId(id: String?) {
//        Logger.e("BaseDataStorage", "saveUserId = $id")
//        saveUserIdPlatform(id)
//        userIdFlow.value = id
//    }
//
//    override suspend fun getUserId(): String? {
//        val id = getUserIdPlatform()
//        userIdFlow.value = id
//        Logger.e("BaseDataStorage", "getUserId = $id")
//        return id
//    }
//
//    protected abstract suspend fun saveUserIdPlatform(id: String?)
//    protected abstract suspend fun getUserIdPlatform(): String?
//
//}