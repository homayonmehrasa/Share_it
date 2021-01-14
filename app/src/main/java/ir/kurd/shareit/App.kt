package ir.kurd.shareit

import android.app.Application
import androidx.multidex.MultiDexApplication
import ir.kurd.shareit.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(viewModelModule)
        }
    }
}