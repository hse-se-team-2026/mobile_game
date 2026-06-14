package ru.hse.mobile_game.screen.model

/** UI state for the save/load screen. */
sealed interface SaveLoadUiState {
    data object Loading : SaveLoadUiState

    data class Ready(val slots: List<SaveSlotUiModel>) : SaveLoadUiState

    data class Error(val message: String) : SaveLoadUiState
}

data class SaveSlotUiModel(
    val id: Long,
    val name: String,
    val chapter: Int,
    val previewText: String?,
    val formattedDate: String,
)
