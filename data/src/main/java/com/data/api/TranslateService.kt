package com.data.api

import com.data.model.TranslationResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

//https://api.mymemory.translated.net/get?q=Hallo&langpair=de|ru
interface TranslateService {
    @GET("get")
    suspend fun translate(
        @Query("q") text: String,
        @Query("langpair") langpair: String
    ): Response<TranslationResponse>
}
