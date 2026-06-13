package ru.hse.mobile_game.data.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

expect class DataStoreFactory {
    fun createDataStore(): DataStore<Preferences>
}
