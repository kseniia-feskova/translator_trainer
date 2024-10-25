package com.data.translate

import com.data.model.WordEntity
import io.ktor.http.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonArray
import java.util.Objects.*

class Translation(
    val targetLanguage: Language,
    val sourceText: String,
    val rawData: String,
    private val url: Url
) {
    private val jsonData = Json.parseToJsonElement(rawData).jsonArray
    private val pronunciation = jsonData[0].jsonArray.last().jsonArray[2].string
    val sourceLanguage = languageOf(jsonData[2].string!!)!!
    val translatedText = buildString { jsonData[0].jsonArray.mapNotNull { it.jsonArray[0].string }.forEach(::append) }
    override fun equals(other: Any?) = when {
        other === this -> true
        other !is Translation -> false
        other.hashCode() == hashCode() -> true
        else -> false
    }

    override fun hashCode() = hash(
        targetLanguage,
        sourceLanguage,
        translatedText,
        pronunciation,
        sourceText,
        jsonData,
        rawData,
        url
    )
    override fun toString() = "Translation($sourceLanguage -> $targetLanguage, $sourceText -> $translatedText)"
}

fun Translation.toWordEntity(): WordEntity {
    return WordEntity(
        word = sourceText,
        translation = translatedText,
        language = sourceLanguage.name
    )
}

private val JsonElement.string
    get() = toString().removeSurrounding("\"").replace("\\n", "\n").takeIf { it != "null" }
