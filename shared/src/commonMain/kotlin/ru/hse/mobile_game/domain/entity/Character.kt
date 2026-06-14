package ru.hse.mobile_game.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class Character(
    val id: String,
    val origin: String,
    val stats: Stats,
    val relations: Map<String, Int> = emptyMap(),
    val factionStandings: Map<String, Int> = emptyMap(),
    val flags: Set<String> = emptySet(),
    val taint: Int = 0,
)

@Serializable
data class Stats(
    val strength: Int = 0,
    val cunning: Int = 0,
    val wisdom: Int = 0,
    val charisma: Int = 0,
)
