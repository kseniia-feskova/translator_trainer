package data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

//https://api.mymemory.translated.net/get?q=Hallo&langpair=de|ru
private const val PROJECT_ID = "translatetrainer-451614"
interface TranslateService {

    @POST("/v3beta1/projects/$PROJECT_ID:translateText")
    suspend fun translateText(
        @Body request: data.model.translate.TranslateRequest
    ): Response<data.model.translate.TranslateResponse>

    @GET("get")
    suspend fun translate(
        @Query("q") text: String,
        @Query("langpair") langpair: String
    ): Response<data.model.TranslationResponse>
}
