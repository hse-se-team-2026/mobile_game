package ru.hse.mobile_game.navigation

import kotlinx.serialization.Serializable

/** Navigation destinations for the app. */
@Serializable
sealed class Screen {
    @Serializable data object MainMenu : Screen()

    @Serializable data object OriginSelect : Screen()

    @Serializable data class Game(val origin: String? = null) : Screen()

    @Serializable data object SaveLoad : Screen()
}
