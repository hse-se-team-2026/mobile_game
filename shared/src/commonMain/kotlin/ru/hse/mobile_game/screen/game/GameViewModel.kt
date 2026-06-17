package ru.hse.mobile_game.screen.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.hse.mobile_game.domain.entity.Character
import ru.hse.mobile_game.domain.entity.GameState
import ru.hse.mobile_game.domain.entity.Scene
import ru.hse.mobile_game.domain.entity.Stats
import ru.hse.mobile_game.domain.usecase.EvaluateConditionsUseCase
import ru.hse.mobile_game.domain.usecase.LoadSceneUseCase
import ru.hse.mobile_game.domain.usecase.MakeChoiceUseCase
import ru.hse.mobile_game.domain.usecase.SaveGameUseCase
import ru.hse.mobile_game.screen.model.CharacterUiModel
import ru.hse.mobile_game.screen.model.ChoiceUiModel
import ru.hse.mobile_game.screen.model.GameUiState

class GameViewModel(
    private val loadScene: LoadSceneUseCase,
    private val makeChoice: MakeChoiceUseCase,
    private val saveGame: SaveGameUseCase,
    private val evaluateConditions: EvaluateConditionsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<GameUiState>(GameUiState.Loading)
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private var gameState: GameState? = null
    private var currentScene: Scene? = null

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

    /** Handle the player selecting a choice. */
    fun onChoiceSelected(choiceId: String) {
        val state = gameState ?: return
        val scene = currentScene ?: return
        val choice = scene.choices.find { it.id == choiceId } ?: return

        viewModelScope.launch {
            try {
                val newState = makeChoice(state, choice)
                gameState = newState.copy(timestamp = currentTimeMillis())

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

                val choiceModels =
                    scene.choices.map { choice ->
                        val available =
                            evaluateConditions.isChoiceAvailable(choice, state.character)
                        ChoiceUiModel(
                            id = choice.id,
                            text = choice.text,
                            isAvailable = available,
                            requirementHint = if (!available) buildRequirementHint(choice) else null,
                        )
                    }

                // Split text into paragraphs on double-newline
                val paragraphs =
                    scene.text
                        .split("\n\n")
                        .map { it.trim() }
                        .filter { it.isNotEmpty() }

                _uiState.value =
                    GameUiState.SceneReady(
                        paragraphs = paragraphs,
                        visibleParagraphs = 1,
                        backgroundAsset = scene.backgroundAsset,
                        choices = choiceModels,
                        character = mapCharacterToUi(state.character),
                        allTextRevealed = paragraphs.size <= 1,
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

    private fun buildRequirementHint(choice: ru.hse.mobile_game.domain.entity.Choice): String {
        val parts = mutableListOf<String>()
        choice.requires?.statMin?.forEach { (stat, min) -> parts.add("$stat ≥ $min") }
        choice.requires?.flagsRequired?.forEach { flag -> parts.add("Requires: $flag") }
        choice.requires?.flagsForbidden?.forEach { flag -> parts.add("Forbidden: $flag") }
        choice.requires?.originRequired?.forEach { origin ->
            val label =
                when (origin) {
                    "noble" -> "Noble"
                    "merchant" -> "Merchant"
                    "soldier" -> "Soldier"
                    "scholar" -> "Scholar"
                    else -> origin
                }
            parts.add("Origin: $label only")
        }
        return parts.joinToString(", ")
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
