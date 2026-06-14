package ru.hse.mobile_game.screen.character

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.hse.mobile_game.screen.model.CharacterUiModel

@Composable
fun CharacterSheet(character: CharacterUiModel) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp),
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

        // Relations section
        if (character.relations.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                SectionHeader("Relations")
                Spacer(modifier = Modifier.height(8.dp))
            }
            items(character.relations.entries.toList()) { (npc, value) ->
                StatRow(npc.replaceFirstChar { it.uppercase() }, value)
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

        // Flags section
        if (character.flags.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                SectionHeader("Traits & Flags")
                Spacer(modifier = Modifier.height(8.dp))
            }
            items(character.flags) { flag ->
                Text(
                    text = "• ${flag.replace("_", " ").replaceFirstChar { it.uppercase() }}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 2.dp),
                )
            }
        }

        item { Spacer(modifier = Modifier.height(32.dp)) }
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
