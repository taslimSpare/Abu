package com.dabinu.abu


import androidx.multidex.MultiDexApplication
import com.dabinu.abu.data.di.appModules
import com.dabinu.abu.data.room.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class AbuApplication: MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        // initialize Room
        val database by lazy { AppDatabase.getDatabase(this) }

        // initialize Koin
        startKoin {
            androidContext(this@AbuApplication)
            modules(appModules)
        }
    }

}
