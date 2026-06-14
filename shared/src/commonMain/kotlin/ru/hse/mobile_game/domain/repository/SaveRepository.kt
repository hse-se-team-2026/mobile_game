package ru.hse.mobile_game.domain.repository

import ru.hse.mobile_game.domain.entity.GameState

interface SaveRepository {
    suspend fun getAll(): List<SaveSlot>

    suspend fun upsert(slot: SaveSlot)

    suspend fun deleteById(id: Long)

    fun serializeState(state: GameState): String

    fun deserializeState(json: String): GameState
}

data class SaveSlot(
    val id: Long,
    val name: String,
    val gameState: GameState,
    val chapter: Int,
    val timestamp: Long,
    val previewText: String?,
)
