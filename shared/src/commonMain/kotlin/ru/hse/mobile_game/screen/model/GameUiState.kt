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
        /** Brief message shown after a manual save, e.g. "Saved: Scene 05 — 15:42". */
        val saveConfirmation: String? = null,
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
 * Outcome shown after a choice — stat changes, relation changes, and/or newly discovered knowledge.
 * Displayed as a popup overlay before the next scene text starts.
 */
data class ChoiceOutcome(
    /** Stat changes with narrative reasons. */
    val statChanges: List<StatChange> = emptyList(),
    /** Relation changes with NPC names and reasons. */
    val relationChanges: List<RelationChange> = emptyList(),
    /** Newly acquired knowledge with descriptions. */
    val newKnowledge: List<KnowledgeGain> = emptyList(),
) {
    val hasContent: Boolean
        get() = statChanges.isNotEmpty() || relationChanges.isNotEmpty() || newKnowledge.isNotEmpty()
}

/** A single stat change with its narrative reason. */
data class StatChange(
    /** Stat name, e.g. "strength". */
    val stat: String,
    /** Delta value, e.g. +1 or -1. */
    val delta: Int,
    /** Why this stat changed — a short narrative explanation. */
    val reason: String,
)

/** A single relation change with the NPC identity and reason. */
data class RelationChange(
    /** NPC key, e.g. "guard_captain". */
    val npcKey: String,
    /** NPC display name, e.g. "Captain Brynn". */
    val npcDisplayName: String,
    /** Delta value, e.g. +2 or -1. */
    val delta: Int,
    /** Why the relation changed — narrative explanation. */
    val reason: String,
)

/** A single piece of newly acquired knowledge (flag) with its description. */
data class KnowledgeGain(
    /** Raw flag id, e.g. "knows_harbor". */
    val flagId: String,
    /** Human-readable title, e.g. "Harbor Secrets". */
    val title: String,
    /** Narrative description of what this knowledge means. */
    val description: String,
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
