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
