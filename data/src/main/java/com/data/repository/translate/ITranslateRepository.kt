package com.data.repository.translate

import com.data.translate.Language

interface ITranslateRepository {

    suspend fun getTranslate(
        text: String,
        originalLanguage: Language,
        resLanguage: Language
    ): String

}