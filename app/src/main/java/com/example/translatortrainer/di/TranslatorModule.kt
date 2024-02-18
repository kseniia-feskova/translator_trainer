package com.example.translatortrainer.di

import com.example.translatortrainer.utils.CustomTranslator
import org.koin.dsl.module

val translatorModule = module {
    single { CustomTranslator() }
}