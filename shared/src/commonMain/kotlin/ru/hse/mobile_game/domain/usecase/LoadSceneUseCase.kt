package ru.hse.mobile_game.domain.usecase

import ru.hse.mobile_game.domain.entity.Scene
import ru.hse.mobile_game.domain.repository.NarrativeRepository

/** Loads a scene by its ID from the narrative repository. */
class LoadSceneUseCase(private val narrativeRepository: NarrativeRepository) {

    suspend operator fun invoke(sceneId: String): Scene {
        return narrativeRepository.getScene(sceneId)
    }
}
