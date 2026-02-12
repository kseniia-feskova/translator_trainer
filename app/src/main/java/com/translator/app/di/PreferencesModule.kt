package com.translator.app.di

import data.prefs.DataStoreManager
import data.CheckToken
import data.prefs.IDataStoreManager
import domain.token.ICheckToken
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import presentation.cache.IUiDataManager
import presentation.cache.UiDataManager

val preferencesModule = module {
    singleOf(::DataStoreManager) bind IDataStoreManager::class
    singleOf(::CheckToken) bind ICheckToken::class
    singleOf(::UiDataManager) bind IUiDataManager::class
}