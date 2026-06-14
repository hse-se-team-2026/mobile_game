package ru.hse.mobile_game.domain.usecase

import ru.hse.mobile_game.domain.entity.GameState
import ru.hse.mobile_game.domain.repository.SaveRepository
import ru.hse.mobile_game.domain.repository.SaveSlot

/**
 * Saves the current game state into a save slot.
 */
class SaveGameUseCase(private val saveRepository: SaveRepository) {

    suspend operator fun invoke(
        slotId: Long,
        name: String,
        gameState: GameState,
        previewText: String? = null,
    ) {
        val slot =
            SaveSlot(
                id = slotId,
                name = name,
                gameState = gameState,
                chapter = gameState.chapter,
                timestamp = gameState.timestamp,
                previewText = previewText,
            )
        saveRepository.upsert(slot)
    }
}
