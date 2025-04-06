package translator.data.translate

/**
 * All languages recognized by Google Translate.
 *
 * @author bush
 * @since 1.0.0
 */
enum class Language(val code: String) {
    AUTO("auto"),

    GERMAN("de"),
    RUSSIAN("ru");

    init {
        languageToEnum[name.lowercase()] = this
        codeToEnum[code] = this
    }

    override fun toString() = name.lowercase().replaceFirstChar { it.uppercase() }
}

private val languageToEnum = mutableMapOf<String, Language>()
private val codeToEnum = mutableMapOf<String, Language>()

/**
 * Attempts to resolve a [Language] from the input string.
 *
 * Valid inputs include "en", "haw", "spanish", "CHINESE_TRA"
 *
 * @param language A language name, code, or part of a
 *                 language name. Case-insensitive.
 *
 * @return The corresponding [Language], or `null` if the input is invalid.
 */
fun languageOf(language: String) = language.lowercase().let { lang ->
    Language.AUTO // Ensure enums are loaded
    codeToEnum[lang] ?: // If language is a code
    languageToEnum[lang] ?: // If language is a string
    languageToEnum[languageToEnum.keys.firstOrNull { lang in it }] // Check for contains
}