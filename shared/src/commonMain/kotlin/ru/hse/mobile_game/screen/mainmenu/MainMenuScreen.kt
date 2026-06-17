package ru.hse.mobile_game.screen.mainmenu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import moblile_game.shared.generated.resources.Res
import moblile_game.shared.generated.resources.bg_main_menu
import org.jetbrains.compose.resources.painterResource

private val DarkBackground = Color(0xFF1A1A2E)
private val GoldAccent = Color(0xFFE0C080)
private val GoldDark = Color(0xFFB8964A)
private val TextLight = Color(0xFFF0EAE0)
private val ButtonBg = Color(0xFF2A2A4E)

@Composable
fun MainMenuScreen(onNewGame: () -> Unit, onLoadGame: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        // Background image
        Image(
            painter = painterResource(Res.drawable.bg_main_menu),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )

        // Dark gradient overlay
        Box(
            modifier =
                Modifier.fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors =
                                listOf(
                                    Color(0xDD1A1A2E),
                                    Color(0x991A1A2E),
                                    Color(0xDD1A1A2E),
                                )
                        )
                    )
        )

        Column(
            modifier = Modifier.widthIn(max = 400.dp).padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            // Decorative emblem
            Text(
                text = "⚜",
                fontSize = 48.sp,
                color = GoldAccent,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Ashes of Gods",
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp,
                color = GoldAccent,
                textAlign = TextAlign.Center,
                letterSpacing = 2.sp,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "A Story-Driven Quest RPG",
                fontSize = 14.sp,
                color = GoldDark,
                textAlign = TextAlign.Center,
                letterSpacing = 1.sp,
            )

            Spacer(modifier = Modifier.height(64.dp))

            Button(
                onClick = onNewGame,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(8.dp),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = ButtonBg,
                        contentColor = GoldAccent,
                    ),
            ) {
                Text("New Game", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onLoadGame,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(8.dp),
            ) {
                Text(
                    "Load Game",
                    fontSize = 18.sp,
                    color = TextLight,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}
