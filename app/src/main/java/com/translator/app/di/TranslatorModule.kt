package com.translator.app.di

import domain.translate.ITranslateModelProvider
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import translate.TranslateModelProvider

val translateModule = module {

    singleOf(::TranslateModelProvider) bind ITranslateModelProvider::class

}
