package com.example.translatortrainer.di

import com.data.translate.CustomTranslator
import org.koin.dsl.module

val translatorModule = module {
    single { CustomTranslator() }
}