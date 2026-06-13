package ru.hse.mobile_game.data.database

import com.example.ashesofgods.database.AppDatabase
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.hse.mobile_game.domain.entity.GameState
import ru.hse.mobile_game.domain.repository.SaveRepository
import ru.hse.mobile_game.domain.repository.SaveSlot

class SaveRepositoryImpl(
    private val database: AppDatabase,
    private val json: Json = Json { ignoreUnknownKeys = true }
) : SaveRepository {

    private val queries = database.saveSlotQueries

    override suspend fun getAll(): List<SaveSlot> {
        return queries.selectAll().executeAsList().map { entity ->
            SaveSlot(
                id = entity.id,
                name = entity.name,
                gameState = deserializeState(entity.game_state),
                chapter = entity.chapter.toInt(),
                timestamp = entity.timestamp,
                previewText = entity.preview_text
            )
        }
    }

    override suspend fun upsert(slot: SaveSlot) {
        queries.upsert(
            id = slot.id,
            name = slot.name,
            game_state = serializeState(slot.gameState),
            chapter = slot.chapter.toLong(),
            timestamp = slot.timestamp,
            preview_text = slot.previewText
        )
    }

    override suspend fun deleteById(id: Long) {
        queries.deleteById(id)
    }

    override fun serializeState(state: GameState): String {
        return json.encodeToString(state)
    }

    override fun deserializeState(json: String): GameState {
        return this.json.decodeFromString(json)
    }
}
