package ru.hse.mobile_game.screen.save

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.hse.mobile_game.domain.entity.GameState
import ru.hse.mobile_game.domain.usecase.LoadGameUseCase
import ru.hse.mobile_game.domain.usecase.SaveGameUseCase
import ru.hse.mobile_game.screen.model.SaveLoadUiState
import ru.hse.mobile_game.screen.model.SaveSlotUiModel

class SaveLoadViewModel(
    private val loadGame: LoadGameUseCase,
    private val saveGame: SaveGameUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<SaveLoadUiState>(SaveLoadUiState.Loading)
    val uiState: StateFlow<SaveLoadUiState> = _uiState.asStateFlow()

    /** Loaded game state when user picks a slot to resume. */
    private var _selectedGameState: GameState? = null
    val selectedGameState: GameState? get() = _selectedGameState

    fun loadSlots() {
        _uiState.value = SaveLoadUiState.Loading
        viewModelScope.launch {
            try {
                val slots = loadGame()
                _uiState.value =
                    SaveLoadUiState.Ready(
                        slots.map { slot ->
                            SaveSlotUiModel(
                                id = slot.id,
                                name = slot.name,
                                chapter = slot.chapter,
                                previewText = slot.previewText,
                                formattedDate = formatTimestamp(slot.timestamp),
                            )
                        }
                    )
            } catch (e: Exception) {
                _uiState.value =
                    SaveLoadUiState.Error("Failed to load saves: ${e.message}")
            }
        }
    }

    fun selectSlot(slotId: Long) {
        viewModelScope.launch {
            try {
                val slots = loadGame()
                val slot = slots.find { it.id == slotId }
                _selectedGameState = slot?.gameState
            } catch (e: Exception) {
                _uiState.value =
                    SaveLoadUiState.Error("Failed to load save: ${e.message}")
            }
        }
    }

    fun saveToSlot(slotId: Long, name: String, gameState: GameState, previewText: String?) {
        viewModelScope.launch {
            try {
                saveGame(slotId, name, gameState, previewText)
                loadSlots() // Refresh the list
            } catch (e: Exception) {
                _uiState.value =
                    SaveLoadUiState.Error("Failed to save: ${e.message}")
            }
        }
    }

    fun deleteSlot(slotId: Long) {
        viewModelScope.launch {
            try {
                loadGame.deleteSlot(slotId)
                loadSlots()
            } catch (e: Exception) {
                _uiState.value =
                    SaveLoadUiState.Error("Failed to delete save: ${e.message}")
            }
        }
    }

    private fun formatTimestamp(timestamp: Long): String {
        // Simple formatting — full date formatting requires platform-specific code
        val seconds = timestamp / 1000
        val minutes = (seconds / 60) % 60
        val hours = (seconds / 3600) % 24
        val days = seconds / 86400
        return "Day $days, ${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}"
    }
}
