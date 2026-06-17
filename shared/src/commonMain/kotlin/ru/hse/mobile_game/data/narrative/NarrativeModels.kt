package ru.hse.mobile_game.data.narrative

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SceneJson(
    val id: String,
    val chapter: Int,
    val background: String,
    val text: String,
    val choices: List<ChoiceJson> = emptyList(),
)

@Serializable
data class ChoiceJson(
    val id: String,
    val text: String,
    val requires: RequirementsJson? = null,
    val effects: EffectsJson? = null,
    @SerialName("next_scene") val nextSceneId: String,
)

@Serializable
data class RequirementsJson(
    @SerialName("stat_min") val statMin: Map<String, Int> = emptyMap(),
    @SerialName("flags_required") val flagsRequired: Set<String> = emptySet(),
    @SerialName("flags_forbidden") val flagsForbidden: Set<String> = emptySet(),
    @SerialName("origin_required") val originRequired: Set<String> = emptySet(),
)

@Serializable
data class EffectsJson(
    val stats: Map<String, Int> = emptyMap(),
    val relations: Map<String, Int> = emptyMap(),
    @SerialName("faction_standings") val factionStandings: Map<String, Int> = emptyMap(),
    val flags: Set<String> = emptySet(),
)
