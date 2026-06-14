package ru.hse.mobile_game.domain.usecase

import ru.hse.mobile_game.domain.entity.Character
import ru.hse.mobile_game.domain.entity.Choice
import ru.hse.mobile_game.domain.entity.Effects
import ru.hse.mobile_game.domain.entity.GameState
import ru.hse.mobile_game.domain.entity.Stats

/**
 * Processes a player's choice: validates requirements, applies effects to the character,
 * and returns an updated GameState pointing to the next scene.
 */
class MakeChoiceUseCase(private val evaluateConditions: EvaluateConditionsUseCase) {

    /**
     * @param currentState current game state
     * @param choice the choice the player selected
     * @throws IllegalStateException if the choice requirements are not met.
     */
    operator fun invoke(currentState: GameState, choice: Choice): GameState {
        check(evaluateConditions.isChoiceAvailable(choice, currentState.character)) {
            "Character does not meet requirements for choice '${choice.id}'"
        }

        val updatedCharacter = applyEffects(currentState.character, choice.effects)

        return currentState.copy(
            character = updatedCharacter,
            currentSceneId = choice.nextSceneId,
            choiceHistory = currentState.choiceHistory + choice.id,
        )
    }

    private fun applyEffects(character: Character, effects: Effects): Character {
        return character.copy(
            stats = applyStatEffects(character.stats, effects.stats),
            relations = mergeIntMaps(character.relations, effects.relations),
            factionStandings = mergeIntMaps(character.factionStandings, effects.factionStandings),
            flags = character.flags + effects.flags,
        )
    }

    private fun applyStatEffects(stats: Stats, statEffects: Map<String, Int>): Stats {
        if (statEffects.isEmpty()) return stats
        return Stats(
            strength = stats.strength + (statEffects["strength"] ?: 0),
            cunning = stats.cunning + (statEffects["cunning"] ?: 0),
            wisdom = stats.wisdom + (statEffects["wisdom"] ?: 0),
            charisma = stats.charisma + (statEffects["charisma"] ?: 0),
        )
    }

    private fun mergeIntMaps(base: Map<String, Int>, delta: Map<String, Int>): Map<String, Int> {
        if (delta.isEmpty()) return base
        val result = base.toMutableMap()
        for ((key, value) in delta) {
            result[key] = (result[key] ?: 0) + value
        }
        return result
    }
}
