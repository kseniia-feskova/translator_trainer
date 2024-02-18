package com.example.translatortrainer.utils

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*


class CustomTranslator(
    private val client: HttpClient = HttpClient(CIO) {
        install(UserAgent) {
            agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64)"
        }
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
) {

    suspend fun translate(
        text: String,
        target: Language,
        source: Language = Language.AUTO
    ): Translation {
        require(target != Language.AUTO) {
            "The target language cannot be Language.AUTO!"
        }
        val response = client.get("https://translate.googleapis.com/translate_a/single") {
            constantParameters()
            parameter("sl", source.code)
            parameter("tl", target.code)
            parameter("hl", target.code)
            parameter("q", text)
        }
        return Translation(target, text, response.bodyAsText(), response.request.url)
    }

}

private val dtParams = arrayOf("at", "bd", "ex", "ld", "md", "qca", "rw", "rm", "ss", "t")

// ^^^
private fun HttpRequestBuilder.constantParameters() {
    parameter("client", "gtx")
    dtParams.forEach { parameter("dt", it) }
    parameter("ie", "UTF-8")
    parameter("oe", "UTF-8")
    parameter("otf", 1)
    parameter("ssel", 0)
    parameter("tsel", 0)
    parameter("tk", "bushissocool")
}

