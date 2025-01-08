package com.presentation.utils

import com.presentation.utils.Language.ARABIC
import com.presentation.utils.Language.CHINESE_TRADITIONAL
import com.presentation.utils.Language.ENGLISH
import com.presentation.utils.Language.FRENCH
import com.presentation.utils.Language.GERMAN
import com.presentation.utils.Language.RUSSIAN
import com.presentation.utils.Language.UKRAINIAN

enum class Language(val code: String) {
    ARABIC("ar"),
    CHINESE_TRADITIONAL("zh-tw"),
    ENGLISH("en"),
    FRENCH("fr"),
    GERMAN("de"),
    RUSSIAN("ru"),
    UKRAINIAN("uk");

    init {
        languageToEnum[name.lowercase()] = this
        codeToEnum[code] = this
    }

    override fun toString() = name.lowercase().replaceFirstChar { it.uppercase() }
}

private val languageToEnum = mutableMapOf<String, Language>()
private val codeToEnum = mutableMapOf<String, Language>()

fun String?.getLanguageByCode(): Language? {
    return when (this) {
        GERMAN.code -> GERMAN
        ENGLISH.code -> ENGLISH
        RUSSIAN.code -> RUSSIAN
        UKRAINIAN.code -> UKRAINIAN
        FRENCH.code -> FRENCH
        ARABIC.code -> ARABIC
        CHINESE_TRADITIONAL.code -> CHINESE_TRADITIONAL
        else -> null
    }
}
