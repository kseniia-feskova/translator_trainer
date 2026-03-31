package com.translator.app.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.translator.app.network.NetworkConnectivityObserver
import data.prefs.DataStoreManager
import data.CheckToken
import data.prefs.IDataStoreManager
import domain.token.ICheckToken
import network.INetworkConnectivityObserver
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import presentation.cache.IUiDataManager
import presentation.cache.UiDataManager

val preferencesModule = module {
    singleOf(::DataStoreManager) bind IDataStoreManager::class
    singleOf(::CheckToken) bind ICheckToken::class
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

    single<INetworkConnectivityObserver> { NetworkConnectivityObserver(get<Context>()) }

}