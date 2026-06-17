package ru.hse.mobile_game.screen.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.delay
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import moblile_game.shared.generated.resources.Res
import moblile_game.shared.generated.resources.bg_alley_night
import moblile_game.shared.generated.resources.bg_city_gate
import moblile_game.shared.generated.resources.bg_dungeon
import moblile_game.shared.generated.resources.bg_guard_post
import moblile_game.shared.generated.resources.bg_harbor
import moblile_game.shared.generated.resources.bg_library
import moblile_game.shared.generated.resources.bg_main_menu
import moblile_game.shared.generated.resources.bg_market_dusk
import moblile_game.shared.generated.resources.bg_noble_estate
import moblile_game.shared.generated.resources.bg_tavern_interior
import moblile_game.shared.generated.resources.bg_temple
import moblile_game.shared.generated.resources.bg_warehouse
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import ru.hse.mobile_game.screen.character.CharacterSheet
import ru.hse.mobile_game.screen.model.ChoiceOutcome
import ru.hse.mobile_game.screen.model.ChoiceUiModel
import ru.hse.mobile_game.screen.model.GameUiState

private fun resolveBackground(backgroundAsset: String): DrawableResource? {
    return when (backgroundAsset) {
        "market_dusk" -> Res.drawable.bg_market_dusk
        "market_day" -> Res.drawable.bg_market_dusk
        "guard_post" -> Res.drawable.bg_guard_post
        "barracks" -> Res.drawable.bg_guard_post
        "city_gate" -> Res.drawable.bg_city_gate
        "garden" -> Res.drawable.bg_city_gate
        "tavern_interior" -> Res.drawable.bg_tavern_interior
        "noble_estate" -> Res.drawable.bg_noble_estate
        "palace_hall" -> Res.drawable.bg_noble_estate
        "council_chamber" -> Res.drawable.bg_noble_estate
        "throne_room" -> Res.drawable.bg_noble_estate
        "warehouse" -> Res.drawable.bg_warehouse
        "alley_night" -> Res.drawable.bg_alley_night
        "harbor" -> Res.drawable.bg_harbor
        "dungeon" -> Res.drawable.bg_dungeon
        "temple" -> Res.drawable.bg_temple
        "cathedral" -> Res.drawable.bg_temple
        "library" -> Res.drawable.bg_library
        "battlefield" -> Res.drawable.bg_guard_post
        else -> Res.drawable.bg_market_dusk
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    viewModel: GameViewModel,
    origin: String?,
    slotId: Long? = null,
    onNavigateToLoad: () -> Unit,
    onNavigateToMenu: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    var showCharacterSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    LaunchedEffect(origin, slotId) {
        when {
            origin != null -> viewModel.startNewGame(origin)
            slotId != null -> viewModel.loadFromSlot(slotId)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = uiState) {
            is GameUiState.Loading -> LoadingContent()
            is GameUiState.SceneReady ->
                SceneContent(
                    state = state,
                    onChoiceSelected = { viewModel.onChoiceSelected(it) },
                    onContinue = { viewModel.revealNextParagraph() },
                    onCharacterClick = { showCharacterSheet = true },
                    onSaveClick = { viewModel.manualSave() },
                    onLoadClick = onNavigateToLoad,
                    onMenuClick = onNavigateToMenu,
                    onDismissOutcome = { viewModel.dismissOutcome() },
                    onDismissSaveConfirmation = { viewModel.dismissSaveConfirmation() },
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
    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xFF1A1A2E)),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(color = Color(0xFFE0C080))
    }
}

@Suppress("DEPRECATION")
@Composable
private fun SceneContent(
    state: GameUiState.SceneReady,
    onChoiceSelected: (String) -> Unit,
    onContinue: () -> Unit,
    onCharacterClick: () -> Unit,
    onSaveClick: () -> Unit,
    onLoadClick: () -> Unit,
    onMenuClick: () -> Unit,
    onDismissOutcome: () -> Unit,
    onDismissSaveConfirmation: () -> Unit,
) {
    val bgDrawable = resolveBackground(state.backgroundAsset)
    var glossaryEntry by remember { mutableStateOf<Glossary.Entry?>(null) }
    val listState = rememberLazyListState()
    val activeTerms = state.activeGlossaryTerms

    // Auto-scroll when new paragraphs are revealed
    LaunchedEffect(state.visibleParagraphs) {
        if (state.visibleParagraphs > 1) {
            listState.animateScrollToItem(state.visibleParagraphs - 1)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background image
        if (bgDrawable != null) {
            Image(
                painter = painterResource(bgDrawable),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        }

        // Dark gradient overlay for readability
        Box(
            modifier =
                Modifier.fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors =
                                listOf(
                                    Color(0xCC1A1A2E),
                                    Color(0xAA1A1A2E),
                                    Color(0xDD1A1A2E),
                                )
                        )
                    )
        )

        // Content
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            // Extra top spacing to push buttons below the status bar area
            Spacer(modifier = Modifier.height(40.dp))

            // Scene title
            Text(
                text = state.sceneName,
                color = Color(0xFFE0C080),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
            )

            // Top bar — two buttons: Character & Menu
            var showMenuPopup by remember { mutableStateOf(false) }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedButton(onClick = onCharacterClick) {
                    Text("📜 Character", color = Color(0xFFE0C080))
                }

                Box {
                    OutlinedButton(onClick = { showMenuPopup = true }) {
                        Text("☰ Menu", color = Color(0xFFE0C080))
                    }
                    DropdownMenu(
                        expanded = showMenuPopup,
                        onDismissRequest = { showMenuPopup = false },
                    ) {
                        DropdownMenuItem(
                            text = { Text("💾 Save") },
                            onClick = {
                                showMenuPopup = false
                                onSaveClick()
                            },
                        )
                        DropdownMenuItem(
                            text = { Text("📂 Load") },
                            onClick = {
                                showMenuPopup = false
                                onLoadClick()
                            },
                        )
                        HorizontalDivider()
                        DropdownMenuItem(
                            text = { Text("🏠 Main Menu") },
                            onClick = {
                                showMenuPopup = false
                                onMenuClick()
                            },
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Scene text (paginated) + choices
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                state = listState,
            ) {
                // Show only visible paragraphs
                val visibleParagraphs = state.paragraphs.take(state.visibleParagraphs)
                items(visibleParagraphs.size) { index ->
                    val paragraph = visibleParagraphs[index]
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xBB1A1A2E)),
                    ) {
                        val annotatedText =
                            remember(paragraph, activeTerms) {
                                parseNarrativeText(paragraph, activeTerms)
                            }
                        ClickableText(
                            text = annotatedText,
                            style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 26.sp),
                            modifier = Modifier.padding(16.dp),
                            onClick = { offset ->
                                annotatedText
                                    .getStringAnnotations(
                                        tag = "glossary",
                                        start = offset,
                                        end = offset,
                                    )
                                    .firstOrNull()
                                    ?.let { annotation ->
                                        glossaryEntry = Glossary.lookup(annotation.item)
                                    }
                            },
                        )
                    }
                }

                // "Continue" button when there's more text
                if (!state.allTextRevealed) {
                    item {
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = onContinue,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors =
                                ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF2A2A4E),
                                    contentColor = Color(0xFFE0C080),
                                ),
                        ) {
                            Text("Continue ▼", fontSize = 16.sp)
                        }
                    }
                }

                // Show choices only when all text is revealed
                if (state.allTextRevealed) {
                    item { Spacer(modifier = Modifier.height(20.dp)) }

                    items(state.choices) { choice ->
                        ChoiceButton(choice = choice, onClick = { onChoiceSelected(choice.id) })
                    }
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }

        // Choice outcome popup overlay (stat changes + relations + knowledge)
        if (state.choiceOutcome != null) {
            ChoiceOutcomePopup(
                outcome = state.choiceOutcome,
                onDismiss = onDismissOutcome,
            )
        }

        // Glossary popup overlay
        if (glossaryEntry != null) {
            GlossaryPopup(
                entry = glossaryEntry!!,
                activeTerms = activeTerms,
                onDismiss = { glossaryEntry = null },
                onTermClick = { term -> glossaryEntry = Glossary.lookup(term) },
            )
        }

        // Save confirmation banner (auto-dismisses after 2 seconds)
        if (state.saveConfirmation != null) {
            LaunchedEffect(state.saveConfirmation) {
                delay(2000)
                onDismissSaveConfirmation()
            }
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter,
            ) {
                Card(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    colors =
                        CardDefaults.cardColors(
                            containerColor = Color(0xDD1A1A2E),
                        ),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text(
                        text = "💾 ${state.saveConfirmation}",
                        color = Color(0xFFE0C080),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun ChoiceOutcomePopup(
    outcome: ChoiceOutcome,
    onDismiss: () -> Unit,
) {
    Box(
        modifier =
            Modifier.fillMaxSize()
                .background(Color(0x99000000))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismiss,
                ),
        contentAlignment = Alignment.Center,
    ) {
        Card(
            modifier =
                Modifier.widthIn(max = 360.dp)
                    .heightIn(max = 500.dp)
                    .padding(24.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {},
                    ),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A2E)),
        ) {
            Column(
                modifier = Modifier.padding(24.dp).verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // ── Stat changes section ──
                if (outcome.statChanges.isNotEmpty()) {
                    Text(
                        text = "⚔ Traits Changed",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE0C080),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    outcome.statChanges.forEach { change ->
                        val sign = if (change.delta > 0) "+" else ""
                        val color =
                            if (change.delta > 0) Color(0xFF88CC88) else Color(0xFFCC6666)
                        val label = change.stat.replaceFirstChar { it.uppercase() }
                        Text(
                            text = "$label $sign${change.delta}",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = color,
                        )
                        Text(
                            text = change.reason,
                            style = MaterialTheme.typography.bodySmall,
                            fontStyle = FontStyle.Italic,
                            color = Color(0xFFCDBFAA),
                            modifier = Modifier.padding(bottom = 4.dp),
                        )
                    }
                }

                // ── Divider between stat and relation sections ──
                if (outcome.statChanges.isNotEmpty() && outcome.relationChanges.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Color(0xFF2A2A4E))
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // ── Relation changes section ──
                if (outcome.relationChanges.isNotEmpty()) {
                    Text(
                        text = "🤝 Relations Changed",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE0C080),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    outcome.relationChanges.forEach { change ->
                        val sign = if (change.delta > 0) "+" else ""
                        val color =
                            if (change.delta > 0) Color(0xFF88CC88) else Color(0xFFCC6666)
                        Text(
                            text = "${change.npcDisplayName} $sign${change.delta}",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = color,
                        )
                        Text(
                            text = change.reason,
                            style = MaterialTheme.typography.bodySmall,
                            fontStyle = FontStyle.Italic,
                            color = Color(0xFFCDBFAA),
                            modifier = Modifier.padding(bottom = 4.dp),
                        )
                    }
                }

                // ── Divider before knowledge section ──
                val hasPreviousSections =
                    outcome.statChanges.isNotEmpty() || outcome.relationChanges.isNotEmpty()
                if (hasPreviousSections && outcome.newKnowledge.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Color(0xFF2A2A4E))
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // ── New knowledge section ──
                if (outcome.newKnowledge.isNotEmpty()) {
                    Text(
                        text = "📖 New Knowledge",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE0C080),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    outcome.newKnowledge.forEach { knowledge ->
                        Text(
                            text = "• ${knowledge.title}",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFF0EAE0),
                        )
                        Text(
                            text = knowledge.description,
                            style = MaterialTheme.typography.bodySmall,
                            fontStyle = FontStyle.Italic,
                            color = Color(0xFFCDBFAA),
                            modifier = Modifier.padding(bottom = 6.dp),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Dismiss button
                Button(
                    onClick = onDismiss,
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2A2A4E),
                            contentColor = Color(0xFFE0C080),
                        ),
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Text("Continue")
                }
            }
        }
    }
}

@Suppress("DEPRECATION")
@Composable
private fun GlossaryPopup(
    entry: Glossary.Entry,
    activeTerms: List<String>,
    onDismiss: () -> Unit,
    onTermClick: (String) -> Unit,
) {
    // Semi-transparent backdrop — dismiss on click
    Box(
        modifier =
            Modifier.fillMaxSize()
                .background(Color(0x99000000))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismiss,
                ),
        contentAlignment = Alignment.Center,
    ) {
        // Card — consume clicks so tapping inside doesn't dismiss
        Card(
            modifier =
                Modifier.widthIn(max = 360.dp)
                    .heightIn(max = 400.dp)
                    .padding(24.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {},
                    ),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A2E)),
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                // Title
                Text(
                    text = entry.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE0C080),
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Scrollable description with glossary term support
                val descriptionAnnotated =
                    remember(entry.description, activeTerms) {
                        parseSingleParagraph(entry.description, activeTerms)
                    }

                Box(
                    modifier =
                        Modifier.weight(1f, fill = false)
                            .verticalScroll(rememberScrollState())
                ) {
                    ClickableText(
                        text = descriptionAnnotated,
                        style =
                            MaterialTheme.typography.bodyMedium.copy(
                                color = Color(0xFFF0EAE0),
                                lineHeight = 22.sp,
                            ),
                        onClick = { offset ->
                            descriptionAnnotated
                                .getStringAnnotations(
                                    tag = "glossary",
                                    start = offset,
                                    end = offset,
                                )
                                .firstOrNull()
                                ?.let { annotation -> onTermClick(annotation.item) }
                        },
                    )
                }
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
                containerColor = Color(0xFF2A2A4E),
                contentColor = Color(0xFFF0EAE0),
                disabledContainerColor = Color(0xFF1A1A2E).copy(alpha = 0.5f),
                disabledContentColor = Color(0xFF888888),
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
                    color = Color(0xFFCC6666),
                )
            }
        }
    }
}

@Composable
private fun ChapterTransitionContent(chapter: Int, summary: String) {
    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xFF1A1A2E)),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.widthIn(max = 400.dp).padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Chapter $chapter",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE0C080),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = summary,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = Color(0xFFF0EAE0),
            )
        }
    }
}

@Composable
private fun GameOverContent(onMenuClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xFF1A1A2E)),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.widthIn(max = 400.dp).padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "End of Demo",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE0C080),
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text =
                    "Your choices have shaped the beginning of this tale.\nThe full story awaits...",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = Color(0xFFF0EAE0),
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onMenuClick,
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6B4E9B),
                        contentColor = Color(0xFFF0EAE0),
                    ),
            ) {
                Text("Return to Menu")
            }
        }
    }
}

@Composable
private fun ErrorContent(message: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xFF1A1A2E)),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.widthIn(max = 400.dp).padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Error",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFFCC6666),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = Color(0xFFF0EAE0),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) { Text("Retry") }
        }
    }
}
