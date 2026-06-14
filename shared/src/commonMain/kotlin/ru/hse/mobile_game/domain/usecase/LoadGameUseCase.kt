package ru.hse.mobile_game.domain.usecase

import ru.hse.mobile_game.domain.repository.SaveRepository
import ru.hse.mobile_game.domain.repository.SaveSlot

/** Loads all available save slots, ordered by timestamp descending. */
class LoadGameUseCase(private val saveRepository: SaveRepository) {

    suspend operator fun invoke(): List<SaveSlot> {
        return saveRepository.getAll()
    }

    suspend fun deleteSlot(slotId: Long) {
        saveRepository.deleteById(slotId)
    }
}
