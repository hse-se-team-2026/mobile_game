package ru.hse.mobile_game.domain.usecase

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import ru.hse.mobile_game.domain.entity.Character
import ru.hse.mobile_game.domain.entity.Choice
import ru.hse.mobile_game.domain.entity.Effects
import ru.hse.mobile_game.domain.entity.GameState
import ru.hse.mobile_game.domain.entity.Requirements
import ru.hse.mobile_game.domain.entity.Stats

class MakeChoiceUseCaseTest {

    private val evaluateConditions = EvaluateConditionsUseCase()
    private val useCase = MakeChoiceUseCase(evaluateConditions)

    private val baseCharacter =
        Character(
            id = "player",
            origin = "noble",
            stats = Stats(strength = 5, cunning = 3, wisdom = 2, charisma = 4),
        )

    private val baseState =
        GameState(
            character = baseCharacter,
            currentSceneId = "scene_01",
            chapter = 1,
            timestamp = 1000L,
        )

    @Test
    fun makeChoiceUpdatesCurrentSceneId() {
        val choice =
            Choice(
                id = "c1",
                text = "Go forward",
                requires = null,
                effects = Effects(),
                nextSceneId = "scene_02",
            )

        val newState = useCase(baseState, choice)
        assertEquals("scene_02", newState.currentSceneId)
    }

    @Test
    fun makeChoiceAddsChoiceIdToHistory() {
        val choice =
            Choice(
                id = "c1",
                text = "Go forward",
                requires = null,
                effects = Effects(),
                nextSceneId = "scene_02",
            )

        val newState = useCase(baseState, choice)
        assertTrue(newState.choiceHistory.contains("c1"))
    }

    @Test
    fun makeChoiceAppliesStatEffects() {
        val choice =
            Choice(
                id = "c1",
                text = "Train",
                requires = null,
                effects = Effects(stats = mapOf("strength" to 2, "cunning" to -1)),
                nextSceneId = "scene_02",
            )

        val newState = useCase(baseState, choice)
        assertEquals(7, newState.character.stats.strength)
        assertEquals(2, newState.character.stats.cunning)
    }

    @Test
    fun makeChoiceAppliesRelationEffects() {
        val choice =
            Choice(
                id = "c1",
                text = "Help the guard",
                requires = null,
                effects = Effects(relations = mapOf("npc_guard" to 5)),
                nextSceneId = "scene_02",
            )

        val newState = useCase(baseState, choice)
        assertEquals(5, newState.character.relations["npc_guard"])
    }

    @Test
    fun makeChoiceMergesRelationEffects() {
        val characterWithRelations = baseCharacter.copy(relations = mapOf("npc_guard" to 3))
        val stateWithRelations = baseState.copy(character = characterWithRelations)

        val choice =
            Choice(
                id = "c1",
                text = "Help the guard again",
                requires = null,
                effects = Effects(relations = mapOf("npc_guard" to 2)),
                nextSceneId = "scene_02",
            )

        val newState = useCase(stateWithRelations, choice)
        assertEquals(5, newState.character.relations["npc_guard"])
    }

    @Test
    fun makeChoiceAppliesFlagEffects() {
        val choice =
            Choice(
                id = "c1",
                text = "Join the guard",
                requires = null,
                effects = Effects(flags = setOf("guard_alliance")),
                nextSceneId = "scene_02",
            )

        val newState = useCase(baseState, choice)
        assertTrue(newState.character.flags.contains("guard_alliance"))
    }

    @Test
    fun makeChoiceThrowsWhenRequirementsNotMet() {
        val choice =
            Choice(
                id = "c1",
                text = "Cast a spell",
                requires = Requirements(statMin = mapOf("wisdom" to 100)),
                effects = Effects(),
                nextSceneId = "scene_02",
            )

        assertFailsWith<IllegalStateException> { useCase(baseState, choice) }
    }

    @Test
    fun makeChoicePreservesChapter() {
        val choice =
            Choice(
                id = "c1",
                text = "Go forward",
                requires = null,
                effects = Effects(),
                nextSceneId = "scene_02",
            )

        val newState = useCase(baseState, choice)
        assertEquals(1, newState.chapter)
    }

    @Test
    fun makeChoiceAppliesFactionStandingEffects() {
        val choice =
            Choice(
                id = "c1",
                text = "Support the faction",
                requires = null,
                effects = Effects(factionStandings = mapOf("rebels" to 10)),
                nextSceneId = "scene_02",
            )

        val newState = useCase(baseState, choice)
        assertEquals(10, newState.character.factionStandings["rebels"])
    }

    @Test
    fun multipleChoicesAccumulateHistory() {
        val choice1 =
            Choice(
                id = "c1",
                text = "First",
                requires = null,
                effects = Effects(),
                nextSceneId = "scene_02",
            )
        val choice2 =
            Choice(
                id = "c2",
                text = "Second",
                requires = null,
                effects = Effects(),
                nextSceneId = "scene_03",
            )

        val state1 = useCase(baseState, choice1)
        val state2 = useCase(state1, choice2)

        assertEquals(listOf("c1", "c2"), state2.choiceHistory)
    }
}
