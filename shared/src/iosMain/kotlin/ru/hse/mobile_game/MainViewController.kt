package ru.hse.mobile_game

import androidx.compose.ui.window.ComposeUIViewController
import org.koin.core.context.startKoin
import ru.hse.mobile_game.di.platformModule
import ru.hse.mobile_game.di.sharedModules

fun MainViewController() = ComposeUIViewController { App() }

fun initKoin() {
    startKoin { modules(sharedModules + platformModule) }
}
