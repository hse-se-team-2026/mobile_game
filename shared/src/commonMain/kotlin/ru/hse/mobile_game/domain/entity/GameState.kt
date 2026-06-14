package ru.hse.mobile_game.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class GameState(
    val character: Character,
    val currentSceneId: String,
    val chapter: Int,
    val choiceHistory: List<String> = emptyList(),
    val timestamp: Long
)
