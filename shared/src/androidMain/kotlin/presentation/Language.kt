package presentation

import android.util.Log
import com.example.translatortrainer.shared.R
import presentation.Language.ENGLISH
import presentation.Language.FRENCH
import presentation.Language.GERMAN
import presentation.Language.RUSSIAN

enum class Language(val code: String) {
    ENGLISH("en"),
    FRENCH("fr"),
    GERMAN("de"),
    RUSSIAN("ru");

    override fun toString() = name.lowercase().replaceFirstChar { it.uppercase() }

    fun getRes(): Int {
       return when (this) {
            GERMAN -> R.string.german
            RUSSIAN -> R.string.russian
            FRENCH -> R.string.french
            ENGLISH -> R.string.english
       }
    }
}

fun String?.getLanguageByCode(): Language {
    return try {
        when (this) {
            GERMAN.code -> GERMAN
            ENGLISH.code -> ENGLISH
            RUSSIAN.code -> RUSSIAN
            FRENCH.code -> FRENCH
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
            ENGLISH.code -> R.drawable.ic_en
            else -> throw Exception("Can not fund the drawable for language")
        }
    } catch (e: Exception) {
        Log.e("getLanguageByCode", "Error = ${e.message}")
        R.drawable.ic_account
    }
}

