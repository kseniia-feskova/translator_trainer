package presentation.utils

import android.util.Log
import com.example.translatortrainer.shared.R
import presentation.utils.Language.ARABIC
import presentation.utils.Language.CHINESE_TRADITIONAL
import presentation.utils.Language.ENGLISH
import presentation.utils.Language.FRENCH
import presentation.utils.Language.GERMAN
import presentation.utils.Language.RUSSIAN
import presentation.utils.Language.UKRAINIAN

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

    fun getRes(): Int {
       return when (this) {
            GERMAN -> R.string.german
            RUSSIAN -> R.string.russian
            FRENCH -> R.string.french
            else -> R.string.german
        }
    }
}

private val languageToEnum = mutableMapOf<String, Language>()
private val codeToEnum = mutableMapOf<String, Language>()

fun String?.getLanguageByCode(): Language {
    return try {
        when (this) {
            GERMAN.code -> GERMAN
            ENGLISH.code -> ENGLISH
            RUSSIAN.code -> RUSSIAN
            UKRAINIAN.code -> UKRAINIAN
            FRENCH.code -> FRENCH
            ARABIC.code -> ARABIC
            CHINESE_TRADITIONAL.code -> CHINESE_TRADITIONAL
            else -> throw Exception("Can not parse language")
        }
    } catch (e: Exception) {
        Log.e("getLanguageByCode", "Error = ${e.message}")
        RUSSIAN
    }
}

fun String?.getResourceByCode(): Int {
    return try {
        when (this) {
            GERMAN.code -> R.drawable.ic_de
            RUSSIAN.code -> R.drawable.ic_ru
            FRENCH.code -> R.drawable.ic_fr
            else -> throw Exception("Can not fund the drawable for language")
        }
    } catch (e: Exception) {
        Log.e("getLanguageByCode", "Error = ${e.message}")
        R.drawable.ic_account
    }
}

