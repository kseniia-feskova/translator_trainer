package com.data.utils

import io.ktor.http.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonArray
import java.util.Objects.*

class Translation internal constructor(
    val targetLanguage: Language,
    val sourceText: String,
    val rawData: String,
    val url: Url
) {
    val jsonData = Json.parseToJsonElement(rawData).jsonArray
    val pronunciation = jsonData[0].jsonArray.last().jsonArray[2].string
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

// Un-json our text
private val JsonElement.string
    get() = toString().removeSurrounding("\"").replace("\\n", "\n").takeIf { it != "null" }
