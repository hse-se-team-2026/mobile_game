package ru.hse.mobile_game

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.hse.mobile_game.di.platformModule
import ru.hse.mobile_game.di.sharedModules

class GameApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@GameApplication)
            modules(sharedModules + platformModule)
        }
    }
}
