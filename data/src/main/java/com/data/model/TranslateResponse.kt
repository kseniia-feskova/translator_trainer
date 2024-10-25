package com.data.model


data class TranslationResponse(
    val responseData: ResponseData,
    val quotaFinished: Boolean,
    val mtLangSupported: Any?,
    val responseDetails: String,
    val responseStatus: Int,
    val responderId: Any?,
    val exceptionCode: Any?,
    val matches: List<Match>
)

data class ResponseData(
    val translatedText: String,
    val match: Double
)

data class Match(
    val id: String,
    val segment: String,
    val translation: String,
    val source: String,
    val target: String,
    val quality: Int,
    val reference: Any?,
    val usageCount: Int,
    val subject: String?,
    val createdBy: String,
    val lastUpdatedBy: String,
    val createDate: String,
    val lastUpdateDate: String,
    val match: Double
)