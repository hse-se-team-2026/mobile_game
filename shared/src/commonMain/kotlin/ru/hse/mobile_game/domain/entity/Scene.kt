package ru.hse.mobile_game.domain.entity

data class Scene(
    val id: String,
    val chapter: Int,
    val backgroundAsset: String,
    val text: String,
    val choices: List<Choice> = emptyList(),
)

data class Choice(
    val id: String,
    val text: String,
    val requires: Requirements?,
    val effects: Effects,
    val nextSceneId: String,
)

data class Requirements(
    val statMin: Map<String, Int> = emptyMap(),
    val flagsRequired: Set<String> = emptySet(),
    val flagsForbidden: Set<String> = emptySet(),
)

data class Effects(
    val stats: Map<String, Int> = emptyMap(),
    val relations: Map<String, Int> = emptyMap(),
    val factionStandings: Map<String, Int> = emptyMap(),
    val flags: Set<String> = emptySet(),
)
