package com.example.translatortrainer.di

import com.presentation.data.DataStoreManager
import com.presentation.data.IDataStoreManager
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val preferencesModule = module {
    singleOf(::DataStoreManager) bind IDataStoreManager::class
}