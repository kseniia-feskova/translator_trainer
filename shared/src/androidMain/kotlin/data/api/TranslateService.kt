package data.api

import data.model.translate.TranslationResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

//https://api.mymemory.translated.net/get?q=Hallo&langpair=de|ru
private const val PROJECT_ID = "translatetrainer-451614"
interface TranslateService {

    @GET("get")
    suspend fun translate(
        @Query("q") text: String,
        @Query("langpair") langpair: String
    ): Response<TranslationResponse>
}
