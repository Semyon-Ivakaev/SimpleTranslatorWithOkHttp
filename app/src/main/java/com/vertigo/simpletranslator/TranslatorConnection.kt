package com.vertigo.simpletranslator

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

interface TranslatorConnectionApi {
    suspend fun getEnglishTranslate()
}

object TranslatorConnectionImpl {
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create())
        .baseUrl("https://microsoft-translator-text.p.rapidapi.com/BreakSentence?api-version=3.0")
        .build()

    private val translateApiService = retrofit.create(TranslatorConnectionApi::class.java)

    suspend fun getEnglishTranslate() {

    }

}