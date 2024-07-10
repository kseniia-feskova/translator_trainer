package com.example.translatortrainer.di

import com.data.utils.CustomTranslator
import org.koin.dsl.module

val translatorModule = module {
    single { CustomTranslator() }
}