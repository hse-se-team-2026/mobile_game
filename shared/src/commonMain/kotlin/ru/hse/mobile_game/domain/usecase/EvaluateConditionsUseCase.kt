package ru.hse.mobile_game.domain.usecase

import ru.hse.mobile_game.domain.entity.Character
import ru.hse.mobile_game.domain.entity.Choice
import ru.hse.mobile_game.domain.entity.Requirements
import ru.hse.mobile_game.domain.entity.Stats

/**
 * Evaluates whether a character meets the requirements for a given choice.
 * Checks stat minimums, required flags, and forbidden flags.
 */
class EvaluateConditionsUseCase {

    fun isChoiceAvailable(choice: Choice, character: Character): Boolean {
        val requirements = choice.requires ?: return true
        return checkStatRequirements(requirements, character.stats) &&
            checkRequiredFlags(requirements, character.flags) &&
            checkForbiddenFlags(requirements, character.flags)
    }

    private fun checkStatRequirements(requirements: Requirements, stats: Stats): Boolean {
        return requirements.statMin.all { (statName, minValue) ->
            getStatValue(stats, statName) >= minValue
        }
    }

    private fun checkRequiredFlags(requirements: Requirements, flags: Set<String>): Boolean {
        return flags.containsAll(requirements.flagsRequired)
    }

    private fun checkForbiddenFlags(requirements: Requirements, flags: Set<String>): Boolean {
        return requirements.flagsForbidden.none { it in flags }
    }

    private fun getStatValue(stats: Stats, statName: String): Int {
        return when (statName.lowercase()) {
            "strength" -> stats.strength
            "cunning" -> stats.cunning
            "wisdom" -> stats.wisdom
            "charisma" -> stats.charisma
            else -> 0
        }
    }
}
