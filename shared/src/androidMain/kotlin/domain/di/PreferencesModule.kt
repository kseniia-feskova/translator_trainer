package domain.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import data.prefs.DataStoreManager
import data.prefs.IDataStoreManager
import data.prefs.ITokenStorage
import data.prefs.TokenStorage
import domain.NetworkConnectivityObserver
import domain.usecases.user.IListenUserIdUseCase
import network.INetworkConnectivityObserver
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import presentation.cache.IUiDataManager
import presentation.cache.UiDataManager
import usecase.ListenUserIdUseCase

val preferencesModule = module {
    singleOf(::ListenUserIdUseCase) bind IListenUserIdUseCase::class
    singleOf(::DataStoreManager) bind IDataStoreManager::class
    singleOf(::UiDataManager) bind IUiDataManager::class
    //TODO: Update with SharedPreferences + own cypher
    single<SharedPreferences> {
        val masterKey = MasterKey.Builder(get<Context>())
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        EncryptedSharedPreferences.create(
            get<Context>(),
            "secure_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    single<ITokenStorage> { TokenStorage(get()) }

    single<INetworkConnectivityObserver> { NetworkConnectivityObserver(get<Context>()) }

}