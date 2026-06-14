package ru.hse.mobile_game.domain.usecase

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import ru.hse.mobile_game.domain.entity.Character
import ru.hse.mobile_game.domain.entity.Choice
import ru.hse.mobile_game.domain.entity.Effects
import ru.hse.mobile_game.domain.entity.Requirements
import ru.hse.mobile_game.domain.entity.Stats

class EvaluateConditionsUseCaseTest {

    private val useCase = EvaluateConditionsUseCase()

    private val baseCharacter =
        Character(
            id = "player",
            origin = "noble",
            stats = Stats(strength = 5, cunning = 3, wisdom = 2, charisma = 4),
            flags = setOf("met_guard", "noble_birth"),
        )

    private fun choice(requires: Requirements? = null) =
        Choice(
            id = "c1",
            text = "Test choice",
            requires = requires,
            effects = Effects(),
            nextSceneId = "scene_02",
        )

    @Test
    fun choiceWithNoRequirementsIsAlwaysAvailable() {
        val result = useCase.isChoiceAvailable(choice(requires = null), baseCharacter)
        assertTrue(result)
    }

    @Test
    fun choiceWithEmptyRequirementsIsAvailable() {
        val result = useCase.isChoiceAvailable(choice(requires = Requirements()), baseCharacter)
        assertTrue(result)
    }

    @Test
    fun choiceWithMetStatMinIsAvailable() {
        val req = Requirements(statMin = mapOf("cunning" to 3))
        val result = useCase.isChoiceAvailable(choice(requires = req), baseCharacter)
        assertTrue(result)
    }

    @Test
    fun choiceWithUnmetStatMinIsUnavailable() {
        val req = Requirements(statMin = mapOf("cunning" to 10))
        val result = useCase.isChoiceAvailable(choice(requires = req), baseCharacter)
        assertFalse(result)
    }

    @Test
    fun choiceWithMultipleStatMinsAllMet() {
        val req = Requirements(statMin = mapOf("strength" to 5, "charisma" to 4))
        val result = useCase.isChoiceAvailable(choice(requires = req), baseCharacter)
        assertTrue(result)
    }

    @Test
    fun choiceWithMultipleStatMinsOneUnmet() {
        val req = Requirements(statMin = mapOf("strength" to 5, "charisma" to 10))
        val result = useCase.isChoiceAvailable(choice(requires = req), baseCharacter)
        assertFalse(result)
    }

    @Test
    fun choiceWithRequiredFlagsMet() {
        val req = Requirements(flagsRequired = setOf("met_guard"))
        val result = useCase.isChoiceAvailable(choice(requires = req), baseCharacter)
        assertTrue(result)
    }

    @Test
    fun choiceWithRequiredFlagsUnmet() {
        val req = Requirements(flagsRequired = setOf("met_king"))
        val result = useCase.isChoiceAvailable(choice(requires = req), baseCharacter)
        assertFalse(result)
    }

    @Test
    fun choiceWithForbiddenFlagsNotPresent() {
        val req = Requirements(flagsForbidden = setOf("criminal"))
        val result = useCase.isChoiceAvailable(choice(requires = req), baseCharacter)
        assertTrue(result)
    }

    @Test
    fun choiceWithForbiddenFlagsPresent() {
        val req = Requirements(flagsForbidden = setOf("met_guard"))
        val result = useCase.isChoiceAvailable(choice(requires = req), baseCharacter)
        assertFalse(result)
    }

    @Test
    fun choiceWithCombinedRequirementsAllMet() {
        val req =
            Requirements(
                statMin = mapOf("strength" to 3),
                flagsRequired = setOf("noble_birth"),
                flagsForbidden = setOf("criminal"),
            )
        val result = useCase.isChoiceAvailable(choice(requires = req), baseCharacter)
        assertTrue(result)
    }

    @Test
    fun unknownStatNameTreatedAsZero() {
        val req = Requirements(statMin = mapOf("luck" to 1))
        val result = useCase.isChoiceAvailable(choice(requires = req), baseCharacter)
        assertFalse(result)
    }
}
