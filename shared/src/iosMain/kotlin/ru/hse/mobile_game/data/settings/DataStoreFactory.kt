package ru.hse.mobile_game.data.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual class DataStoreFactory {

    @OptIn(ExperimentalForeignApi::class)
    actual fun createDataStore(): DataStore<Preferences> {
        val documentDirectory =
            NSFileManager.defaultManager
                .URLForDirectory(
                    directory = NSDocumentDirectory,
                    inDomain = NSUserDomainMask,
                    appropriateForURL = null,
                    create = false,
                    shouldExpand = false,
                )
                ?.path ?: throw IllegalStateException("Document directory not found")

        return preferencesDataStore(name = "$documentDirectory/settings.preferences_pb")
    }
}
