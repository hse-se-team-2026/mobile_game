package ru.hse.mobile_game.data.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

actual class DataStoreFactory(private val context: Context) {
    
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    
    actual fun createDataStore(): DataStore<Preferences> {
        return context.dataStore
    }
}
