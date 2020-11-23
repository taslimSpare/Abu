package com.dabinu.abu.data

import com.dabinu.abu.models.ConvertResponse
import com.dabinu.abu.models.SymbolsResponse
import retrofit2.http.GET
import retrofit2.http.Query


/*
This interface contains the endpoints that are being consuming directly from fixer.io
*/
interface ApiService {


    // this method converts a value from one currency to another

    @GET("convert")
    suspend fun convert(
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("amount")amount: Double
    ): ConvertResponse



    // this function returns a map of all currencies and their three-letter abbreviations

    @GET("symbols")
    suspend fun symbols(): SymbolsResponse


}
