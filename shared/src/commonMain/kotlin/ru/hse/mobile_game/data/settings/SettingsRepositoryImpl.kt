package ru.hse.mobile_game.data.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import ru.hse.mobile_game.domain.repository.SettingsRepository

class SettingsRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {

    companion object {
        private val MUSIC_VOLUME_KEY = floatPreferencesKey("music_volume")
        private val SFX_VOLUME_KEY = floatPreferencesKey("sfx_volume")
        private val LANGUAGE_KEY = stringPreferencesKey("language")
        
        private const val DEFAULT_MUSIC_VOLUME = 0.7f
        private const val DEFAULT_SFX_VOLUME = 0.8f
        private const val DEFAULT_LANGUAGE = "en"
    }

    override suspend fun getMusicVolume(): Float {
        return dataStore.data.first()[MUSIC_VOLUME_KEY] ?: DEFAULT_MUSIC_VOLUME
    }

    override suspend fun setMusicVolume(volume: Float) {
        dataStore.edit { preferences ->
            preferences[MUSIC_VOLUME_KEY] = volume
        }
    }

    override suspend fun getSfxVolume(): Float {
        return dataStore.data.first()[SFX_VOLUME_KEY] ?: DEFAULT_SFX_VOLUME
    }

    override suspend fun setSfxVolume(volume: Float) {
        dataStore.edit { preferences ->
            preferences[SFX_VOLUME_KEY] = volume
        }
    }

    override suspend fun getLanguage(): String {
        return dataStore.data.first()[LANGUAGE_KEY] ?: DEFAULT_LANGUAGE
    }

    override suspend fun setLanguage(lang: String) {
        dataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = lang
        }
    }
}
