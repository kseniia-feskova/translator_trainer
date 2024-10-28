package com.data.repository.translate

interface ITranslateRepository {

    suspend fun getTranslate(
        text: String,
    ): String

}