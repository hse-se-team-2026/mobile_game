package ru.hse.mobile_game.screen.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.hse.mobile_game.domain.entity.Character
import ru.hse.mobile_game.domain.entity.Choice
import ru.hse.mobile_game.domain.entity.GameState
import ru.hse.mobile_game.domain.entity.Scene
import ru.hse.mobile_game.domain.entity.Stats
import ru.hse.mobile_game.domain.usecase.EvaluateConditionsUseCase
import ru.hse.mobile_game.domain.usecase.LoadGameUseCase
import ru.hse.mobile_game.domain.usecase.LoadSceneUseCase
import ru.hse.mobile_game.domain.usecase.MakeChoiceUseCase
import ru.hse.mobile_game.domain.usecase.SaveGameUseCase
import ru.hse.mobile_game.screen.model.CharacterUiModel
import ru.hse.mobile_game.screen.model.ChoiceOutcome
import ru.hse.mobile_game.screen.model.ChoiceUiModel
import ru.hse.mobile_game.screen.model.GameUiState
import ru.hse.mobile_game.screen.model.KnowledgeGain
import ru.hse.mobile_game.screen.model.RelationChange
import ru.hse.mobile_game.screen.model.StatChange

class GameViewModel(
    private val loadScene: LoadSceneUseCase,
    private val makeChoice: MakeChoiceUseCase,
    private val saveGame: SaveGameUseCase,
    private val evaluateConditions: EvaluateConditionsUseCase,
    private val loadGame: LoadGameUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<GameUiState>(GameUiState.Loading)
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private var gameState: GameState? = null
    private var currentScene: Scene? = null

    /** Outcome from the last choice, shown as a popup on the next scene. */
    private var pendingOutcome: ChoiceOutcome? = null

    /** Start a new game with the given origin. */
    fun startNewGame(origin: String) {
        val initialCharacter =
            Character(
                id = "player",
                origin = origin,
                stats = statsForOrigin(origin),
            )
        val initialState =
            GameState(
                character = initialCharacter,
                currentSceneId = "scene_01",
                chapter = 1,
                timestamp = currentTimeMillis(),
            )
        gameState = initialState
        loadCurrentScene()
    }

    /** Resume a game from a saved state. */
    fun resumeGame(savedState: GameState) {
        gameState = savedState
        loadCurrentScene()
    }

    /** Load a game from a specific save slot by id. */
    fun loadFromSlot(slotId: Long) {
        _uiState.value = GameUiState.Loading
        viewModelScope.launch {
            try {
                val slots = loadGame()
                val slot = slots.find { it.id == slotId }
                if (slot != null) {
                    gameState = slot.gameState
                    loadCurrentScene()
                } else {
                    _uiState.value = GameUiState.Error("Save slot not found")
                }
            } catch (e: Exception) {
                _uiState.value = GameUiState.Error("Failed to load save: ${e.message}")
            }
        }
    }

    /** Reveal the next paragraph of text. */
    fun revealNextParagraph() {
        val state = _uiState.value
        if (state is GameUiState.SceneReady && !state.allTextRevealed) {
            val newVisible = (state.visibleParagraphs + 1).coerceAtMost(state.paragraphs.size)
            _uiState.value =
                state.copy(
                    visibleParagraphs = newVisible,
                    allTextRevealed = newVisible >= state.paragraphs.size,
                )
        }
    }

    /** Dismiss the choice outcome popup. */
    fun dismissOutcome() {
        val state = _uiState.value
        if (state is GameUiState.SceneReady && state.choiceOutcome != null) {
            _uiState.value = state.copy(choiceOutcome = null)
        }
    }

    /** Handle the player selecting a choice. */
    fun onChoiceSelected(choiceId: String) {
        val state = gameState ?: return
        val scene = currentScene ?: return
        val choice = scene.choices.find { it.id == choiceId } ?: return

        viewModelScope.launch {
            try {
                val oldCharacter = state.character
                val newState = makeChoice(state, choice)
                gameState = newState.copy(timestamp = currentTimeMillis())

                // Compute outcome: stat changes + relation changes + new knowledge
                pendingOutcome = computeOutcome(oldCharacter, newState.character, choice)

                // Auto-save after each choice
                autoSave()

                // Load the next scene
                loadCurrentScene()
            } catch (e: IllegalStateException) {
                _uiState.value = GameUiState.Error(e.message ?: "Cannot make this choice")
            } catch (e: Exception) {
                _uiState.value = GameUiState.Error("Unexpected error: ${e.message}")
            }
        }
    }

    /** Save the current game to a specific slot. */
    fun saveToSlot(slotId: Long, name: String) {
        val state = gameState ?: return
        viewModelScope.launch {
            try {
                saveGame(
                    slotId = slotId,
                    name = name,
                    gameState = state,
                    previewText = currentScene?.text?.take(100),
                )
            } catch (e: Exception) {
                _uiState.value = GameUiState.Error("Failed to save: ${e.message}")
            }
        }
    }

    private fun loadCurrentScene() {
        val state = gameState ?: return

        // Handle end-of-content marker
        if (state.currentSceneId == "end") {
            _uiState.value = GameUiState.GameOver
            return
        }

        _uiState.value = GameUiState.Loading

        viewModelScope.launch {
            try {
                val scene = loadScene(state.currentSceneId)
                currentScene = scene

                // Filter: only show choices that pass origin + flag checks (visibility).
                // Choices that fail stat checks are shown as disabled with a hint.
                val choiceModels =
                    scene.choices
                        .filter { choice ->
                            evaluateConditions.isChoiceVisible(choice, state.character)
                        }
                        .map { choice ->
                            val available =
                                evaluateConditions.isChoiceAvailable(choice, state.character)
                            ChoiceUiModel(
                                id = choice.id,
                                text = choice.text,
                                isAvailable = available,
                                requirementHint =
                                    if (!available) buildRequirementHint(choice) else null,
                            )
                        }

                // Split text into paragraphs on double-newline
                val paragraphs =
                    scene.text
                        .split("\n\n")
                        .map { it.trim() }
                        .filter { it.isNotEmpty() }

                // Compute unlocked glossary terms based on player flags
                val activeTerms = Glossary.unlockedTerms(state.character.flags)

                // Pick up any pending outcome from the last choice
                val outcome = pendingOutcome?.takeIf { it.hasContent }
                pendingOutcome = null

                _uiState.value =
                    GameUiState.SceneReady(
                        paragraphs = paragraphs,
                        visibleParagraphs = 1,
                        backgroundAsset = scene.backgroundAsset,
                        choices = choiceModels,
                        character = mapCharacterToUi(state.character),
                        allTextRevealed = paragraphs.size <= 1,
                        activeGlossaryTerms = activeTerms,
                        choiceOutcome = outcome,
                    )
            } catch (e: Exception) {
                _uiState.value = GameUiState.Error("Failed to load scene: ${e.message}")
            }
        }
    }

    private suspend fun autoSave() {
        val state = gameState ?: return
        try {
            saveGame(
                slotId = 0L,
                name = "Autosave",
                gameState = state,
                previewText = currentScene?.text?.take(100),
            )
        } catch (_: Exception) {
            // Autosave failure is non-critical
        }
    }

    /**
     * Compute rich outcome from a choice — stat changes with reasons, relation changes with NPC
     * names and reasons, and new knowledge with descriptions.
     */
    private fun computeOutcome(
        oldCharacter: Character,
        newCharacter: Character,
        choice: Choice,
    ): ChoiceOutcome {
        // ── Stat changes with narrative reasons ──
        val statChanges =
            choice.effects.stats
                .filter { it.value != 0 }
                .map { (stat, delta) ->
                    StatChange(
                        stat = stat,
                        delta = delta,
                        reason = buildStatReason(stat, delta, choice),
                    )
                }

        // ── Relation changes with NPC names and reasons ──
        val relationChanges =
            choice.effects.relations
                .filter { it.value != 0 }
                .map { (npcKey, delta) ->
                    RelationChange(
                        npcKey = npcKey,
                        npcDisplayName = NpcRegistry.displayName(npcKey),
                        delta = delta,
                        reason = buildRelationReason(npcKey, delta, choice),
                    )
                }

        // ── New flags → knowledge gains with descriptions ──
        val newFlags = newCharacter.flags - oldCharacter.flags
        val newKnowledge =
            newFlags.map { flagId ->
                val info = FlagRegistry.lookup(flagId)
                KnowledgeGain(
                    flagId = flagId,
                    title = info?.title ?: formatFlagFallback(flagId),
                    description = info?.description ?: "You have acquired new knowledge.",
                )
            }

        return ChoiceOutcome(
            statChanges = statChanges,
            relationChanges = relationChanges,
            newKnowledge = newKnowledge,
        )
    }

    /** Build a narrative reason for a stat change based on the choice context. */
    private fun buildStatReason(stat: String, delta: Int, choice: Choice): String {
        val choiceText = choice.text.take(REASON_PREVIEW_LENGTH)
        val verb = if (delta > 0) "increased" else "decreased"
        val statLabel = stat.replaceFirstChar { it.uppercase() }
        return "Your $statLabel $verb from choosing: \"$choiceText\""
    }

    /** Build a narrative reason for a relation change based on the choice context. */
    private fun buildRelationReason(npcKey: String, delta: Int, choice: Choice): String {
        val npcName = NpcRegistry.displayName(npcKey)
        val choiceText = choice.text.take(REASON_PREVIEW_LENGTH)
        return if (delta > 0) {
            "$npcName appreciates your decision: \"$choiceText\""
        } else {
            "$npcName disapproves of your decision: \"$choiceText\""
        }
    }

    /** Fallback title for flags not in the registry. */
    private fun formatFlagFallback(flag: String): String {
        return flag
            .removePrefix("met_")
            .removePrefix("knows_")
            .removePrefix("visited_")
            .replace("_", " ")
            .replaceFirstChar { it.uppercase() }
    }

    private fun mapCharacterToUi(character: Character): CharacterUiModel {
        return CharacterUiModel(
            id = character.id,
            origin = character.origin,
            strength = character.stats.strength,
            cunning = character.stats.cunning,
            wisdom = character.stats.wisdom,
            charisma = character.stats.charisma,
            taint = character.taint,
            flags = character.flags.toList(),
            relations = character.relations,
            factionStandings = character.factionStandings,
        )
    }

    /** Build hint showing only stat requirements (origin/flags are hidden, not hinted). */
    private fun buildRequirementHint(choice: Choice): String {
        val parts = mutableListOf<String>()
        choice.requires?.statMin?.forEach { (stat, min) -> parts.add("$stat ≥ $min") }
        return if (parts.isEmpty()) "Requirements not met" else parts.joinToString(", ")
    }

    companion object {
        private const val REASON_PREVIEW_LENGTH = 60
    }
}

private fun statsForOrigin(origin: String): Stats {
    return when (origin) {
        "noble" -> Stats(strength = 1, cunning = 2, wisdom = 2, charisma = 5)
        "merchant" -> Stats(strength = 1, cunning = 5, wisdom = 2, charisma = 2)
        "soldier" -> Stats(strength = 5, cunning = 2, wisdom = 1, charisma = 2)
        "scholar" -> Stats(strength = 1, cunning = 2, wisdom = 5, charisma = 2)
        else -> Stats(strength = 2, cunning = 2, wisdom = 2, charisma = 2)
    }
}

internal expect fun currentTimeMillis(): Long
