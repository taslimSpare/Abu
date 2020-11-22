package com.dabinu.abu


import androidx.multidex.MultiDexApplication
import com.dabinu.abu.di.appModules
import com.dabinu.abu.room.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class AbuApplication: MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        // initialize Koin
        startKoin {
            androidContext(this@AbuApplication)
            modules(appModules)
        }
    }

}
