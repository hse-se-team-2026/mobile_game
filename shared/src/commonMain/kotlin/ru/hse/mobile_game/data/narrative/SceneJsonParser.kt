package ru.hse.mobile_game.data.narrative

import kotlinx.serialization.json.Json
import ru.hse.mobile_game.domain.entity.Choice
import ru.hse.mobile_game.domain.entity.Effects
import ru.hse.mobile_game.domain.entity.Requirements
import ru.hse.mobile_game.domain.entity.Scene

class SceneJsonParser(private val json: Json = Json { ignoreUnknownKeys = true }) {
    fun parseScene(jsonString: String): SceneJson {
        return try {
            json.decodeFromString(SceneJson.serializer(), jsonString)
        } catch (e: Exception) {
            throw IllegalArgumentException("Failed to parse scene JSON: ${e.message}", e)
        }
    }

    fun parseChapterIndex(jsonString: String): Map<String, String> {
        return try {
            json.decodeFromString<Map<String, String>>(jsonString)
        } catch (e: Exception) {
            throw IllegalArgumentException("Failed to parse chapter index JSON: ${e.message}", e)
        }
    }

    fun mapToDomain(sceneJson: SceneJson): Scene {
        return Scene(
            id = sceneJson.id,
            title = sceneJson.title.ifEmpty { sceneJson.id },
            chapter = sceneJson.chapter,
            backgroundAsset = sceneJson.background,
            text = sceneJson.text,
            choices = sceneJson.choices.map { mapChoiceToDomain(it) },
        )
    }

    private fun mapChoiceToDomain(choiceJson: ChoiceJson): Choice {
        return Choice(
            id = choiceJson.id,
            text = choiceJson.text,
            requires = choiceJson.requires?.let { mapRequirementsToDomain(it) },
            effects = choiceJson.effects?.let { mapEffectsToDomain(it) } ?: Effects(),
            nextSceneId = choiceJson.nextSceneId,
        )
    }

    private fun mapRequirementsToDomain(requirementsJson: RequirementsJson): Requirements {
        return Requirements(
            statMin = requirementsJson.statMin,
            flagsRequired = requirementsJson.flagsRequired,
            flagsForbidden = requirementsJson.flagsForbidden,
            originRequired = requirementsJson.originRequired,
        )
    }

    private fun mapEffectsToDomain(effectsJson: EffectsJson): Effects {
        return Effects(
            stats = effectsJson.stats,
            relations = effectsJson.relations,
            factionStandings = effectsJson.factionStandings,
            flags = effectsJson.flags,
        )
    }
}
