package ru.hse.mobile_game.screen.character

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.hse.mobile_game.screen.game.FlagRegistry
import ru.hse.mobile_game.screen.game.Glossary
import ru.hse.mobile_game.screen.game.NpcRegistry
import ru.hse.mobile_game.screen.model.CharacterUiModel

@Composable
fun CharacterSheet(character: CharacterUiModel) {
    var selectedFlag by remember { mutableStateOf<String?>(null) }
    var selectedNpcKey by remember { mutableStateOf<String?>(null) }

    Box(modifier = Modifier.fillMaxWidth()) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            item {
                Text(
                    text = "Character",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Origin: ${character.origin.replaceFirstChar { it.uppercase() }}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Stats section
            item {
                SectionHeader("Stats")
                Spacer(modifier = Modifier.height(8.dp))
            }
            item { StatRow("Strength", character.strength) }
            item { StatRow("Cunning", character.cunning) }
            item { StatRow("Wisdom", character.wisdom) }
            item { StatRow("Charisma", character.charisma) }
            item { StatRow("Taint", character.taint) }

            // Relations section with NPC names from registry
            if (character.relations.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    SectionHeader("Relations")
                    Spacer(modifier = Modifier.height(8.dp))
                }
                items(character.relations.entries.toList()) { (npc, value) ->
                    val displayName = NpcRegistry.displayName(npc)
                    val hasGlossary = Glossary.glossaryKeyForNpc(npc) != null
                    ClickableStatRow(
                        label = displayName,
                        value = value,
                        clickable = hasGlossary,
                        onClick = { selectedNpcKey = npc },
                    )
                }
            }

            // Faction standings
            if (character.factionStandings.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    SectionHeader("Faction Standings")
                    Spacer(modifier = Modifier.height(8.dp))
                }
                items(character.factionStandings.entries.toList()) { (faction, value) ->
                    StatRow(faction.replaceFirstChar { it.uppercase() }, value)
                }
            }

            // Chronicle section — clickable flags with detail popup
            if (character.flags.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    SectionHeader("Chronicle")
                    Spacer(modifier = Modifier.height(8.dp))
                }
                items(character.flags) { flag ->
                    val title = FlagRegistry.displayTitle(flag)
                    Text(
                        text = "• $title",
                        style =
                            MaterialTheme.typography.bodyMedium.copy(
                                textDecoration = TextDecoration.Underline,
                            ),
                        color = Color(0xFFE0C080),
                        modifier =
                            Modifier.padding(vertical = 2.dp)
                                .clickable { selectedFlag = flag },
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }
        }

        // Flag detail popup overlay
        if (selectedFlag != null) {
            FlagDetailPopup(
                flagId = selectedFlag!!,
                onDismiss = { selectedFlag = null },
            )
        }

        // NPC glossary popup overlay
        if (selectedNpcKey != null) {
            NpcGlossaryPopup(
                npcKey = selectedNpcKey!!,
                onDismiss = { selectedNpcKey = null },
                onTermClick = { glossaryKey ->
                    // Navigate from one glossary entry to another via cross-link
                    selectedNpcKey = null
                    // We could open the linked term, but for simplicity we just close
                },
            )
        }
    }
}

/** Popup showing detailed info about a Chronicle flag entry. */
@Composable
private fun FlagDetailPopup(
    flagId: String,
    onDismiss: () -> Unit,
) {
    val info = FlagRegistry.lookup(flagId)
    val title = info?.title ?: flagId.replace("_", " ").replaceFirstChar { it.uppercase() }

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
                    .heightIn(max = 460.dp)
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
                modifier = Modifier.padding(20.dp).verticalScroll(rememberScrollState()),
            ) {
                // Title
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE0C080),
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (info != null) {
                    // Description
                    Text(
                        text = info.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFFF0EAE0),
                        lineHeight = 22.sp,
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Color(0xFF2A2A4E))
                    Spacer(modifier = Modifier.height(12.dp))

                    // How obtained
                    Text(
                        text = "📜 How obtained",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFE0C080),
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = info.howObtained,
                        style = MaterialTheme.typography.bodySmall,
                        fontStyle = FontStyle.Italic,
                        color = Color(0xFFCDBFAA),
                        lineHeight = 20.sp,
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Usage hint
                    Text(
                        text = "💡 Significance",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFE0C080),
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = info.hint,
                        style = MaterialTheme.typography.bodySmall,
                        fontStyle = FontStyle.Italic,
                        color = Color(0xFFCDBFAA),
                        lineHeight = 20.sp,
                    )
                } else {
                    Text(
                        text = "Details not yet recorded in the chronicle.",
                        style = MaterialTheme.typography.bodyMedium,
                        fontStyle = FontStyle.Italic,
                        color = Color(0xFFCDBFAA),
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Close button
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2A2A4E),
                            contentColor = Color(0xFFE0C080),
                        ),
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Text("Close")
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
        )
        HorizontalDivider(modifier = Modifier.padding(top = 4.dp))
    }
}

@Composable
private fun StatRow(label: String, value: Int) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label.replace("_", " ").replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.bodyMedium,
        )
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color =
                when {
                    value > 0 -> MaterialTheme.colorScheme.primary
                    value < 0 -> MaterialTheme.colorScheme.error
                    else -> MaterialTheme.colorScheme.onSurface
                },
        )
    }
}

/** A stat row where the label is clickable (gold + underlined) to open a detail popup. */
@Composable
private fun ClickableStatRow(
    label: String,
    value: Int,
    clickable: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        if (clickable) {
            Text(
                text = label,
                style =
                    MaterialTheme.typography.bodyMedium.copy(
                        textDecoration = TextDecoration.Underline,
                    ),
                color = Color(0xFFE0C080),
                modifier = Modifier.clickable(onClick = onClick),
            )
        } else {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color =
                when {
                    value > 0 -> MaterialTheme.colorScheme.primary
                    value < 0 -> MaterialTheme.colorScheme.error
                    else -> MaterialTheme.colorScheme.onSurface
                },
        )
    }
}

/** Popup showing glossary info about an NPC, including relation flavor. */
@Composable
private fun NpcGlossaryPopup(
    npcKey: String,
    onDismiss: () -> Unit,
    onTermClick: (String) -> Unit,
) {
    val glossaryKey = Glossary.glossaryKeyForNpc(npcKey)
    val glossaryEntry = glossaryKey?.let { Glossary.lookup(it) }
    val npcInfo = NpcRegistry.lookup(npcKey)
    val displayName = npcInfo?.displayName ?: NpcRegistry.displayName(npcKey)

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
                modifier = Modifier.padding(20.dp).verticalScroll(rememberScrollState()),
            ) {
                // Title
                Text(
                    text = displayName,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE0C080),
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Glossary description
                if (glossaryEntry != null) {
                    Text(
                        text = glossaryEntry.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFFF0EAE0),
                        lineHeight = 22.sp,
                    )
                } else if (npcInfo != null) {
                    Text(
                        text = npcInfo.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFFF0EAE0),
                        lineHeight = 22.sp,
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = Color(0xFF2A2A4E))
                Spacer(modifier = Modifier.height(12.dp))

                // Close button
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2A2A4E),
                            contentColor = Color(0xFFE0C080),
                        ),
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Text("Close")
                }
            }
        }
    }
}
