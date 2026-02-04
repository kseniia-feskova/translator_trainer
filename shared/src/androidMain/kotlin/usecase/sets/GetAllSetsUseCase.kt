package usecase.sets

import data.prefs.IDataStoreManager
import data.repository.set.ISetApiRepository
import data.repository.set.ISetDaoRepository
import domain.cache.ISetsCacheProvider
import domain.token.ICheckToken
import domain.token.ITokenRefresher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import mapper.toUI
import presentation.model.SetOfCards
import presentation.usecases.course.ICoursesOnPrefsUseCases
import presentation.usecases.sets.IGetAllSetsUseCase

class GetAllSetsUseCase(
    private val repo: ISetApiRepository,
    private val dao: ISetDaoRepository,
    private val cache: ISetsCacheProvider,
    private val tokenRefresher: ITokenRefresher,
    private val checkToken: ICheckToken,
    private val prefs: IDataStoreManager,
    private val coursePrefs: ICoursesOnPrefsUseCases
) : IGetAllSetsUseCase {

    override fun invokeFlow(): Flow<List<SetOfCards>> = flow {
        val course = coursePrefs.getCourse()
        if (course != null) {
            val cached = cache.getSets()?.map { it.toUI() }
            if (cached != null) {
                emit(cached)
            } else {
                if (!prefs.isOfflineMode() && !prefs.isGuest()) {
                    val response = checkToken.safeApiCallWithRefresh(
                        call = { repo.getAllSets(course.id) },
                        onTokenExpired = { tokenRefresher.refreshToken() }
                    )
                    response.data?.let {
                        cache.updateSets(it)
                        dao.saveSets(it)
                    }
                } else {
                    dao.getAllSetsFlow(course.id).first()
                }
                emitAll(dao.getAllSetsFlow(course.id).map { sets -> sets.map { it.toUI() } })
            }
        }
    }

    override suspend fun invoke(): Result<List<SetOfCards>> {
        val course = coursePrefs.getCourse()
        if (course != null) {
            val cached = cache.getSets()?.map { it.toUI() }
            if (cached != null) {
                return Result.success(cached)
            } else {
                val response = if (prefs.isGuest() || prefs.isOfflineMode()) {
                    dao.getAllSets(course.id)
                } else {
                    checkToken.safeApiCallWithRefresh(
                        call = { repo.getAllSets(course.id) },
                        onTokenExpired = { tokenRefresher.refreshToken() })
                }
                val data = response.data
                return if (response.errorMsg.isNotEmpty()) {
                    if (response.errorMsg.contains("Failed to connect")) {
                        Result.failure(Exception("Failed to connect"))
                    } else Result.failure(Exception(response.errorMsg))
                } else if (data == null) {
                    Result.failure(Exception("Empty user data"))
                } else {
                    cache.updateSets(data)
                    Result.success(data.map { it.toUI() })
                }
            }
        } else return Result.failure(Exception("No course selected"))
    }
}