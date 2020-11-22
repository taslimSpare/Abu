package com.dabinu.abu.data


import androidx.multidex.MultiDexApplication
import com.dabinu.abu.data.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class AbuApplication: MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@AbuApplication)
            modules(appModules)
        }
    }

}