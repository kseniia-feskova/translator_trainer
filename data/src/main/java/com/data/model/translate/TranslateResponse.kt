package com.data.model.translate

data class TranslateResponse(
    val translations: List<Translation>
)

data class Translation(
    val translatedText: String
)