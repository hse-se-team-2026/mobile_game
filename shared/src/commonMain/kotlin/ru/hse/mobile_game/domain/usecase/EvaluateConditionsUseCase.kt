package ru.hse.mobile_game.domain.usecase

import ru.hse.mobile_game.domain.entity.Character
import ru.hse.mobile_game.domain.entity.Choice
import ru.hse.mobile_game.domain.entity.Requirements
import ru.hse.mobile_game.domain.entity.Stats

/**
 * Evaluates whether a character meets the requirements for a given choice. Separates *visibility*
 * (origin + flags → choice is hidden entirely if unmet) from *availability* (stats → choice shown
 * but disabled with a hint).
 */
class EvaluateConditionsUseCase {

    /** Returns true if the choice should be SHOWN to the player (origin + flag gates). */
    fun isChoiceVisible(choice: Choice, character: Character): Boolean {
        val requirements = choice.requires ?: return true
        return checkOriginRequirement(requirements, character.origin) &&
            checkRequiredFlags(requirements, character.flags) &&
            checkForbiddenFlags(requirements, character.flags)
    }

    /** Returns true if the choice can actually be SELECTED (all requirements including stats). */
    fun isChoiceAvailable(choice: Choice, character: Character): Boolean {
        val requirements = choice.requires ?: return true
        return checkStatRequirements(requirements, character.stats) &&
            checkRequiredFlags(requirements, character.flags) &&
            checkForbiddenFlags(requirements, character.flags) &&
            checkOriginRequirement(requirements, character.origin)
    }

    /** Returns true if the choice fails ONLY on stats (visible but disabled). */
    fun failsOnlyOnStats(choice: Choice, character: Character): Boolean {
        val requirements = choice.requires ?: return false
        val statOk = checkStatRequirements(requirements, character.stats)
        val originOk = checkOriginRequirement(requirements, character.origin)
        val flagsOk =
            checkRequiredFlags(requirements, character.flags) &&
                checkForbiddenFlags(requirements, character.flags)
        return !statOk && originOk && flagsOk
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

    private fun checkOriginRequirement(requirements: Requirements, origin: String): Boolean {
        if (requirements.originRequired.isEmpty()) return true
        return origin in requirements.originRequired
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
