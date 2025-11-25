package domain.translate

import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import data.translate.Language
import domain.mapper.toTranslatorModel

//TODO add more or make dynamic for different languages
private fun createRuToDeTranslator() =
    Translation.getClient(
        TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.RUSSIAN)
            .setTargetLanguage(TranslateLanguage.GERMAN)
            .build()
    )

private fun createDeToRuTranslator() =
    Translation.getClient(
        TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.GERMAN)
            .setTargetLanguage(TranslateLanguage.RUSSIAN)
            .build()
    )

interface ITranslateModelProvider {
    fun translate(
        text: String,
        sourceLanguage: Language,
        targetLanguage: Language,
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit
    )

    fun downloadModel(
        sourceLanguage: Language,
        targetLanguage: Language,
        onError: (Exception) -> Unit
    )

    fun close()
}

class TranslateModelProvider : ITranslateModelProvider {

    private var translator: Translator? = null

    override fun translate(
        text: String,
        sourceLanguage: Language,
        targetLanguage: Language,
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        if (sourceLanguage == Language.RUSSIAN && targetLanguage == Language.GERMAN) {
            translateRuToDe(text, onSuccess, onError)
        }
        if (sourceLanguage == Language.GERMAN && targetLanguage == Language.RUSSIAN) {
            translateDeToRu(text, onSuccess, onError)
        }
    }

    override fun downloadModel(
        sourceLanguage: Language, targetLanguage: Language, onError: (Exception) -> Unit
    ) {
        Translation.getClient(
            TranslatorOptions.Builder()
                .setSourceLanguage(sourceLanguage.toTranslatorModel())
                .setTargetLanguage(targetLanguage.toTranslatorModel())
                .build()
        ).downloadModelIfNeeded().addOnFailureListener {
            onError(it)
        }

    }

    fun translateRuToDe(text: String, callback: (String) -> Unit, onError: (Exception) -> Unit) {
        translator = createRuToDeTranslator()
        translator?.run {
            downloadModelIfNeeded()
                .addOnSuccessListener {
                    translator?.translate(text)
                        ?.addOnSuccessListener { callback(it) }
                        ?.addOnFailureListener { callback("Ошибка перевода: ${it.message}") }
                }
                .addOnFailureListener { onError(it) }
        }
    }

    fun translateDeToRu(text: String, callback: (String) -> Unit, onError: (Exception) -> Unit) {
        translator = createDeToRuTranslator()
        translator?.run {
            downloadModelIfNeeded()
                .addOnSuccessListener {
                    translator?.translate(text)
                        ?.addOnSuccessListener { callback(it) }
                        ?.addOnFailureListener { callback("Ошибка перевода: ${it.message}") }
                }
                .addOnFailureListener { onError(it) }
        }
    }

    override fun close() {
        translator?.close()
        translator = null
    }
}