package com.dabinu.abu.data

import com.dabinu.abu.models.ConvertResponse
import com.dabinu.abu.models.LatestResponse
import retrofit2.http.Field
import retrofit2.http.GET


interface ApiService {

    @GET("convert")
    suspend fun convert(
        @Field("from") from: String,
        @Field("to") to: String,
        @Field("amount")amount: Double
    ): ConvertResponse


    @GET("latest")
    suspend fun latest(
        @Field("base") base: String,
        @Field("symbols") symbols: String
    ): LatestResponse


}