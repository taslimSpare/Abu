package com.dabinu.abu.data.di

import androidx.room.Room
import com.dabinu.abu.data.data.ApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import org.koin.dsl.module
import retrofit2.Retrofit
import com.dabinu.abu.BuildConfig
import com.dabinu.abu.data.data.FirebaseHelper
import com.dabinu.abu.data.data.RoomDB
import com.dabinu.abu.data.room.AppDatabase
import com.dabinu.abu.data.viewmodels.AuthViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import retrofit2.converter.gson.GsonConverterFactory


private const val API_BASE_URL = BuildConfig.BASE_URL
private const val ACCESS_TOKEN = BuildConfig.ACCESS_TOKEN

val appModules = module {
    single { createApiService() }
    single { FirebaseHelper() }
    single { RoomDB(get()) }
    single { Room.databaseBuilder(androidContext(), AppDatabase::class.java, "abu_db").build() }
    single { get<AppDatabase>().accountDao() }
    viewModel { AuthViewModel(get(), get()) }
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