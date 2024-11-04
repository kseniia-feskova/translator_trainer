package com.domain.di

import com.domain.usecase.AddWordUseCase
import com.domain.usecase.GetSetOfCardsUseCase
import com.domain.usecase.IAddWordUseCase
import com.domain.usecase.IGetSetOfCardsUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val useCaseModule = module {

    singleOf(::AddWordUseCase) bind IAddWordUseCase::class

    singleOf(::GetSetOfCardsUseCase) bind IGetSetOfCardsUseCase::class
}