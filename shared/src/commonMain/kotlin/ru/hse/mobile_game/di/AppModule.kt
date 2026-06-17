package ru.hse.mobile_game.di

import org.koin.dsl.module
import ru.hse.mobile_game.data.database.SaveRepositoryImpl
import ru.hse.mobile_game.data.narrative.NarrativeRepositoryImpl
import ru.hse.mobile_game.data.narrative.SceneJsonParser
import ru.hse.mobile_game.data.settings.SettingsRepositoryImpl
import ru.hse.mobile_game.domain.repository.NarrativeRepository
import ru.hse.mobile_game.domain.repository.SaveRepository
import ru.hse.mobile_game.domain.repository.SettingsRepository
import ru.hse.mobile_game.domain.usecase.EvaluateConditionsUseCase
import ru.hse.mobile_game.domain.usecase.LoadGameUseCase
import ru.hse.mobile_game.domain.usecase.LoadSceneUseCase
import ru.hse.mobile_game.domain.usecase.MakeChoiceUseCase
import ru.hse.mobile_game.domain.usecase.SaveGameUseCase
import ru.hse.mobile_game.screen.game.GameViewModel
import ru.hse.mobile_game.screen.save.SaveLoadViewModel

val domainModule = module {
    factory { EvaluateConditionsUseCase() }
    factory { LoadSceneUseCase(get()) }
    factory { MakeChoiceUseCase(get()) }
    factory { SaveGameUseCase(get()) }
    factory { LoadGameUseCase(get()) }
}

val dataModule = module {
    single { SceneJsonParser() }
    single<NarrativeRepository> { NarrativeRepositoryImpl(get()) }
    single<SaveRepository> { SaveRepositoryImpl(get()) }
    single<SettingsRepository> { SettingsRepositoryImpl(get()) }
}

val viewModelModule = module {
    factory { GameViewModel(get(), get(), get(), get(), get()) }
    factory { SaveLoadViewModel(get(), get()) }
}

/** Common modules to be included in platform-specific Koin initialization. */
val sharedModules = listOf(domainModule, dataModule, viewModelModule)
