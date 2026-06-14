package ru.hse.mobile_game.domain.repository

interface SettingsRepository {
    suspend fun getMusicVolume(): Float

    suspend fun setMusicVolume(volume: Float)

    suspend fun getSfxVolume(): Float

    suspend fun setSfxVolume(volume: Float)

    suspend fun getLanguage(): String

    suspend fun setLanguage(lang: String)
}
