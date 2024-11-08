package com.domain.di

import com.domain.usecase.AddSetOfWordsUseCase
import com.domain.usecase.AddSetWordCrossRef
import com.domain.usecase.AddWordUseCase
import com.domain.usecase.GetFilteredWordsUseCase
import com.domain.usecase.GetSetOfAllCardsUseCase
import com.domain.usecase.GetSetOfCardsUseCase
import com.domain.usecase.GetSetOfWordsUseCase
import com.domain.usecase.GetWordByIdUseCase
import com.domain.usecase.GetWordsOfSetUseCase
import com.domain.usecase.IAddSetOfWordsUseCase
import com.domain.usecase.IAddSetWordCrossRef
import com.domain.usecase.IAddWordUseCase
import com.domain.usecase.IGetFilteredWordsUseCase
import com.domain.usecase.IGetSetOfAllCardsUseCase
import com.domain.usecase.IGetSetOfCardsUseCase
import com.domain.usecase.IGetSetOfWordsUseCase
import com.domain.usecase.IGetWordByIdUseCase
import com.domain.usecase.IGetWordsOfSetUseCase
import com.domain.usecase.IUpdateWordUseCase
import com.domain.usecase.UpdateWordUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val useCaseModule = module {

    singleOf(::AddWordUseCase) bind IAddWordUseCase::class

    singleOf(::GetSetOfCardsUseCase) bind IGetSetOfCardsUseCase::class

    singleOf(::GetSetOfAllCardsUseCase) bind IGetSetOfAllCardsUseCase::class

    singleOf(::GetWordsOfSetUseCase) bind IGetWordsOfSetUseCase::class

    singleOf(::AddSetOfWordsUseCase) bind IAddSetOfWordsUseCase::class

    singleOf(::GetSetOfWordsUseCase) bind IGetSetOfWordsUseCase::class

    singleOf(::GetFilteredWordsUseCase) bind IGetFilteredWordsUseCase::class

    singleOf(::AddSetWordCrossRef) bind IAddSetWordCrossRef::class

    singleOf(::UpdateWordUseCase) bind IUpdateWordUseCase::class

    singleOf(::GetWordByIdUseCase) bind IGetWordByIdUseCase::class

}