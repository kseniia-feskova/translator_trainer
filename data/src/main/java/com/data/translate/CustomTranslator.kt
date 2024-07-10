package com.data.translate

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*


private val client: HttpClient = HttpClient(CIO) {
    HttpResponseValidator {
        validateResponse { response ->
            if (!response.status.isSuccess()) {
                throw TranslationException("Error caught from HTTP request: ${response.status}")
            }
        }
        handleResponseExceptionWithRequest { cause, _ ->
            if (cause !is TranslationException) {
                throw TranslationException("Exception caught from HTTP request", cause)
            }
        }
    }
}
private val dtParams = arrayOf("at", "bd", "ex", "ld", "md", "qca", "rw", "rm", "ss", "t")
private val BASE_URL = "https://translate.googleapis.com/translate_a/single"

class CustomTranslator {

    suspend fun translate(
        text: String,
        target: Language,
        source: Language = Language.AUTO
    ): Translation {
        require(target != Language.AUTO) {
            "The target language cannot be Language.AUTO!"
        }
        val response = client.get(BASE_URL) {
            constantParameters()
            parameter("sl", source.code) //source language
            parameter("tl", target.code) //translate language
            parameter("hl", target.code) //
            parameter("q", text) //query text
        }
        return Translation(target, text, response.bodyAsText(), response.request.url)
    }
}

private fun HttpRequestBuilder.constantParameters() {
    parameter("client", "gtx")
    dtParams.forEach { parameter("dt", it) }
    parameter("ie", "UTF-8")
    parameter("oe", "UTF-8")
    parameter("otf", 1)
    parameter("ssel", 0)
    parameter("tsel", 0)
}