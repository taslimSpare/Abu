package com.dabinu.abu.data

import com.dabinu.abu.models.ConvertResponse
import com.dabinu.abu.models.SymbolsResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {


    @GET("convert")
    suspend fun convert(
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("amount")amount: Double
    ): ConvertResponse


    @GET("symbols")
    suspend fun symbols(): SymbolsResponse


}