package ru.hse.mobile_game.screen.model

/** Represents the UI state of the game screen. */
sealed interface GameUiState {
    data object Loading : GameUiState

    data class SceneReady(
        val paragraphs: List<String>,
        val visibleParagraphs: Int,
        val backgroundAsset: String,
        val choices: List<ChoiceUiModel>,
        val character: CharacterUiModel,
        val allTextRevealed: Boolean,
        val activeGlossaryTerms: List<String>,
        val choiceOutcome: ChoiceOutcome? = null,
    ) : GameUiState

    data class ChapterTransition(val chapter: Int, val summaryText: String) : GameUiState

    data object GameOver : GameUiState

    data class Error(val message: String) : GameUiState
}

data class ChoiceUiModel(
    val id: String,
    val text: String,
    val isAvailable: Boolean,
    val requirementHint: String? = null,
)

/**
 * Outcome shown after a choice — stat changes and/or newly discovered knowledge. Displayed as a
 * popup overlay before the next scene text starts.
 */
data class ChoiceOutcome(
    /** Stat deltas, e.g. mapOf("strength" to 1, "wisdom" to -1). */
    val statChanges: Map<String, Int> = emptyMap(),
    /** Newly acquired flags in human-readable form, e.g. ["Marta", "Edric"]. */
    val newKnowledge: List<String> = emptyList(),
) {
    val hasContent: Boolean
        get() = statChanges.isNotEmpty() || newKnowledge.isNotEmpty()
}

data class CharacterUiModel(
    val id: String,
    val origin: String,
    val strength: Int,
    val cunning: Int,
    val wisdom: Int,
    val charisma: Int,
    val taint: Int,
    val flags: List<String>,
    val relations: Map<String, Int>,
    val factionStandings: Map<String, Int>,
)
