package com.dabinu.abu.data.di

import android.content.Context
import com.dabinu.abu.data.data.ApiService
import com.dabinu.abu.data.data.session.Session
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import com.dabinu.abu.BuildConfig
import retrofit2.converter.gson.GsonConverterFactory


private val API_BASE_URL = BuildConfig.BASE_URL
private val ACCESS_TOKEN = BuildConfig.ACCESS_TOKEN
private val PREF_NAME = "abu_prefs"

val appModules = module {
    single { Session(androidContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)) }
    factory { get<Session>().getAccount() }
    single { createApiService() }
}


private fun createApiService(): ApiService {
    val retrofit = initRetrofit()
    return retrofit.create(ApiService::class.java)
}

private fun initRetrofit(): Retrofit {

    return Retrofit.Builder()
        .client(getOkHttpClient())
        .baseUrl(API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}


private fun getOkHttpClient() : OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(TokenInterceptor())
        .build()
}



class TokenInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val url = chain
            .request()
            .url
            .newBuilder()
            .addQueryParameter("access_key", ACCESS_TOKEN)
            .build()

        return chain.proceed(chain.request().newBuilder().url(url).build())

    }
}