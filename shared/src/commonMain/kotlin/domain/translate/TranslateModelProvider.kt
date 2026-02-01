package domain.translate

import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import data.translate.Language
import domain.mapper.toTranslatorModel

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
        translateWithModel(
            text,
            sourceLanguage.toTranslatorModel(),
            targetLanguage.toTranslatorModel(),
            onSuccess, onError
        )
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

    fun translateWithModel(
        text: String,
        sourceLanguage: String,
        targetLanguage: String,
        callback: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        translator = Translation.getClient(
            TranslatorOptions.Builder()
                .setSourceLanguage(sourceLanguage)
                .setTargetLanguage(targetLanguage)
                .build()
        )
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