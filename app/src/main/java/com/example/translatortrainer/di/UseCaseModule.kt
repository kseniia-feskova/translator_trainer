package com.example.translatortrainer.di

import com.domain.usecase.AddSetOfWordsUseCase
import com.domain.usecase.AddSetWordCrossRef
import com.domain.usecase.AddWordUseCase
import com.domain.usecase.DeleteSetByIdUseCase
import com.domain.usecase.FindWordByOriginUseCase
import com.domain.usecase.FindWordByTranslatedUseCase
import com.domain.usecase.GetAllSetsUseCase
import com.domain.usecase.GetFilteredWordsUseCase
import com.domain.usecase.GetSetOfAllCardsUseCase
import com.domain.usecase.GetSetOfCardsUseCase
import com.domain.usecase.GetSetOfWordsUseCase
import com.domain.usecase.GetWordByIdUseCase
import com.domain.usecase.GetWordsOfSetUseCase
import com.domain.usecase.TranslateWordUseCase
import com.domain.usecase.UpdateWordUseCase
import com.presentation.usecases.IAddSetOfWordsUseCase
import com.presentation.usecases.IAddSetWordCrossRefUseCase
import com.presentation.usecases.IAddWordUseCase
import com.presentation.usecases.IDeleteSetByIdUseCase
import com.presentation.usecases.IFindWordByOriginUseCase
import com.presentation.usecases.IFindWordByTranslatedUseCase
import com.presentation.usecases.IGetAllSetsUseCase
import com.presentation.usecases.IGetFilteredWordsUseCase
import com.presentation.usecases.IGetSetOfAllCardsUseCase
import com.presentation.usecases.IGetSetOfCardsUseCase
import com.presentation.usecases.IGetSetOfWordsUseCase
import com.presentation.usecases.IGetWordByIdUseCase
import com.presentation.usecases.IGetWordsOfSetUseCase
import com.presentation.usecases.ITranslateWordUseCase
import com.presentation.usecases.IUpdateWordUseCase
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

    singleOf(::AddSetWordCrossRef) bind IAddSetWordCrossRefUseCase::class

    singleOf(::UpdateWordUseCase) bind IUpdateWordUseCase::class

    singleOf(::GetWordByIdUseCase) bind IGetWordByIdUseCase::class

    singleOf(::TranslateWordUseCase) bind ITranslateWordUseCase::class

    singleOf(::DeleteSetByIdUseCase) bind IDeleteSetByIdUseCase::class

    singleOf(::FindWordByOriginUseCase) bind IFindWordByOriginUseCase::class

    singleOf(::FindWordByTranslatedUseCase) bind IFindWordByTranslatedUseCase::class

    singleOf(::GetAllSetsUseCase) bind IGetAllSetsUseCase::class
}