package ru.hse.mobile_game.di

import com.example.ashesofgods.database.AppDatabase
import org.koin.dsl.module
import ru.hse.mobile_game.data.database.DatabaseDriverFactory
import ru.hse.mobile_game.data.settings.DataStoreFactory

val platformModule = module {
    single { DatabaseDriverFactory() }
    single { get<DatabaseDriverFactory>().createDriver() }
    single { AppDatabase(get()) }
    single { DataStoreFactory() }
    single { get<DataStoreFactory>().createDataStore() }
}
