package com.jamiltonmentoria.nexusstore

import android.app.Application
import com.jamiltonmentoria.nexusstore.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * Application class for initializing Koin dependency injection.
 */
class DummyStoreApp : Application() {
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidLogger()
            androidContext(this@DummyStoreApp)
            modules(appModule)
        }
    }
}
