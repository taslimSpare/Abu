package com.dabinu.abu.di

import androidx.room.Room
import com.dabinu.abu.data.ApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import org.koin.dsl.module
import retrofit2.Retrofit
import com.dabinu.abu.BuildConfig
import com.dabinu.abu.data.FirebaseHelper
import com.dabinu.abu.data.RoomDB
import com.dabinu.abu.room.AppDatabase
import com.dabinu.abu.room.dao.AccountDao
import com.dabinu.abu.viewmodels.AuthViewModel
import com.dabinu.abu.viewmodels.FixerViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import retrofit2.converter.gson.GsonConverterFactory



/* The fields below are saved in the buildConfig for easier management and scalability.
   This would also be useful if there are different values per buildType in the future
 */
private const val API_BASE_URL = BuildConfig.BASE_URL
private const val ACCESS_TOKEN = BuildConfig.ACCESS_TOKEN



val appModules = module {
    single { createApiService() }
    single { FirebaseHelper() }
    single { RoomDB(get() as AccountDao) }
    single { Room.databaseBuilder(androidContext(),
        AppDatabase::class.java, "abu_db")
        .allowMainThreadQueries()
        .build() } // using allowMainThreadQueries() is highly discouraged as running on the UI thread can lead to ANRs. A better alternative would have been to run my DB queries on a network thread using Coroutines.
    single { get<AppDatabase>().accountDao() }
    viewModel { AuthViewModel(get(), get()) }
    viewModel { FixerViewModel(get(), get()) }
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



/* The main purpose of this interceptor is to pass in the
   access_key parameter since it's required for every API call to fixer.io.
   This approach is preferable to manually adding it in
   every call.
 */
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
