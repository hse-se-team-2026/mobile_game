package ru.hse.mobile_game.screen.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.hse.mobile_game.screen.character.CharacterSheet
import ru.hse.mobile_game.screen.model.ChoiceUiModel
import ru.hse.mobile_game.screen.model.GameUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    viewModel: GameViewModel,
    origin: String?,
    onNavigateToSave: () -> Unit,
    onNavigateToMenu: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    var showCharacterSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    LaunchedEffect(origin) {
        if (origin != null) {
            viewModel.startNewGame(origin)
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)) {
        when (val state = uiState) {
            is GameUiState.Loading -> LoadingContent()
            is GameUiState.SceneReady ->
                SceneContent(
                    state = state,
                    onChoiceSelected = { viewModel.onChoiceSelected(it) },
                    onCharacterClick = { showCharacterSheet = true },
                    onSaveClick = onNavigateToSave,
                    onMenuClick = onNavigateToMenu,
                )
            is GameUiState.ChapterTransition ->
                ChapterTransitionContent(chapter = state.chapter, summary = state.summaryText)
            is GameUiState.GameOver -> GameOverContent(onMenuClick = onNavigateToMenu)
            is GameUiState.Error ->
                ErrorContent(message = state.message, onRetry = { /* retry logic */ })
        }

        if (showCharacterSheet) {
            val state = uiState
            if (state is GameUiState.SceneReady) {
                ModalBottomSheet(
                    onDismissRequest = { showCharacterSheet = false },
                    sheetState = sheetState,
                ) {
                    CharacterSheet(character = state.character)
                }
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun SceneContent(
    state: GameUiState.SceneReady,
    onChoiceSelected: (String) -> Unit,
    onCharacterClick: () -> Unit,
    onSaveClick: () -> Unit,
    onMenuClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Top bar with action buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = "⚔", fontSize = 24.sp, modifier = Modifier.padding(8.dp))

            Row {
                OutlinedButton(
                    onClick = onCharacterClick,
                    modifier = Modifier.padding(end = 8.dp),
                ) {
                    Text("Character")
                }
                OutlinedButton(onClick = onSaveClick, modifier = Modifier.padding(end = 8.dp)) {
                    Text("Save")
                }
                OutlinedButton(onClick = onMenuClick) { Text("Menu") }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Scene text
        LazyColumn(modifier = Modifier.weight(1f).fillMaxWidth()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors =
                        CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                ) {
                    Text(
                        text = state.sceneText,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(16.dp),
                        lineHeight = 24.sp,
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            // Choices
            items(state.choices) { choice ->
                ChoiceButton(choice = choice, onClick = { onChoiceSelected(choice.id) })
            }
        }
    }
}

@Composable
private fun ChoiceButton(choice: ChoiceUiModel, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = choice.isAvailable,
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        colors =
            ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                disabledContainerColor =
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            ),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Text(text = choice.text, style = MaterialTheme.typography.bodyMedium)
            if (choice.requirementHint != null) {
                Text(
                    text = choice.requirementHint,
                    style = MaterialTheme.typography.bodySmall,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                )
            }
        }
    }
}

@Composable
private fun ChapterTransitionContent(chapter: Int, summary: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.widthIn(max = 400.dp).padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Chapter $chapter",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = summary,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
private fun GameOverContent(onMenuClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.widthIn(max = 400.dp).padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "The End",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = onMenuClick) { Text("Return to Menu") }
        }
    }
}

@Composable
private fun ErrorContent(message: String, onRetry: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.widthIn(max = 400.dp).padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Error",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.error,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) { Text("Retry") }
        }
    }
}
