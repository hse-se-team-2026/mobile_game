package ru.hse.mobile_game.screen.origin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import moblile_game.shared.generated.resources.bg_main_menu
import org.jetbrains.compose.resources.painterResource

private data class OriginOption(
    val id: String,
    val title: String,
    val description: String,
    val stats: String,
)

private val origins =
    listOf(
        OriginOption(
            id = "noble",
            title = "\uD83D\uDC51 Noble",
            description =
                "Born into the ruling class of Ashenmoor, you command respect and know the language of power. " +
                    "Doors open at the mention of your family name — but privilege breeds enemies.",
            stats = "STR 1 · CUN 2 · WIS 2 · CHA 5",
        ),
        OriginOption(
            id = "merchant",
            title = "\uD83D\uDCB0 Merchant",
            description =
                "Raised among traders and coin-counters in the harbor district, you see opportunity " +
                    "where others see chaos. Every person has a price — you just need to find it.",
            stats = "STR 1 · CUN 5 · WIS 2 · CHA 2",
        ),
        OriginOption(
            id = "soldier",
            title = "⚔\uFE0F Soldier",
            description =
                "Forged in the border wars, you know discipline, sacrifice, and the weight of a blade. " +
                    "The battlefield taught you that words are cheap — strength decides who lives.",
            stats = "STR 5 · CUN 2 · WIS 1 · CHA 2",
        ),
        OriginOption(
            id = "scholar",
            title = "\uD83D\uDCDA Scholar",
            description =
                "A life spent among ancient texts in the Great Library has sharpened your mind beyond measure. " +
                    "You see patterns where others see mystery — but the world beyond books is harsh.",
            stats = "STR 1 · CUN 2 · WIS 5 · CHA 2",
        ),
    )

@Composable
fun OriginSelectScreen(onOriginSelected: (String) -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Background
        Image(
            painter = painterResource(Res.drawable.bg_main_menu),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
        Box(
            modifier =
                Modifier.fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors =
                                listOf(
                                    Color(0xDD1A1A2E),
                                    Color(0xCC1A1A2E),
                                    Color(0xEE1A1A2E),
                                )
                        )
                    )
        )

        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Choose Your Origin",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE0C080),
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text =
                    "Your origin shapes your starting abilities and how the world perceives you.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFCDBFAA),
                textAlign = TextAlign.Center,
                fontStyle = FontStyle.Italic,
            )

            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(origins) { origin ->
                    OriginCard(origin = origin, onClick = { onOriginSelected(origin.id) })
                }
            }
        }
    }
}

@Composable
private fun OriginCard(origin: OriginOption, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xBB2A2A4E)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = origin.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    color = Color(0xFFE0C080),
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = origin.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFF0EAE0),
                lineHeight = 20.sp,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = origin.stats,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF88AACC),
                letterSpacing = 1.sp,
            )
        }
    }
}
