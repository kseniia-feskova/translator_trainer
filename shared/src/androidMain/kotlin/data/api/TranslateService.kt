package data.api

import data.model.translate.TranslationResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

//https://api.mymemory.translated.net/get?q=Hallo&langpair=de|ru
private const val PROJECT_ID = "translatetrainer-451614"
private const val MY_MEMORY_API = "https://api.mymemory.translated.net"

class TranslateService(
    private val client: HttpClient
) {

    suspend fun translate(text: String, langpair: String): TranslationResponse{
        return client.get("${MY_MEMORY_API}get"){
            parameter("q", text)
            parameter("langpair", langpair)
        }.body()
    }

}
