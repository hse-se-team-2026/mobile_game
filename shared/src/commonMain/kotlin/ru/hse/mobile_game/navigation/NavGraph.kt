package ru.hse.mobile_game.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import org.koin.compose.viewmodel.koinViewModel
import ru.hse.mobile_game.screen.game.GameScreen
import ru.hse.mobile_game.screen.game.GameViewModel
import ru.hse.mobile_game.screen.mainmenu.MainMenuScreen
import ru.hse.mobile_game.screen.origin.OriginSelectScreen
import ru.hse.mobile_game.screen.save.SaveLoadScreen
import ru.hse.mobile_game.screen.save.SaveLoadViewModel

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.MainMenu) {
        composable<Screen.MainMenu> {
            MainMenuScreen(
                onNewGame = { navController.navigate(Screen.OriginSelect) },
                onLoadGame = { navController.navigate(Screen.SaveLoad) },
            )
        }

        composable<Screen.OriginSelect> {
            OriginSelectScreen(
                onOriginSelected = { origin ->
                    navController.navigate(Screen.Game(origin = origin)) {
                        popUpTo<Screen.MainMenu>()
                    }
                }
            )
        }

        composable<Screen.Game> { backStackEntry ->
            val route = backStackEntry.toRoute<Screen.Game>()
            val gameViewModel: GameViewModel = koinViewModel()
            GameScreen(
                viewModel = gameViewModel,
                origin = route.origin,
                slotId = route.slotId,
                onNavigateToLoad = { navController.navigate(Screen.SaveLoad) },
                onNavigateToMenu = {
                    navController.navigate(Screen.MainMenu) {
                        popUpTo<Screen.MainMenu> { inclusive = true }
                    }
                },
            )
        }

        composable<Screen.SaveLoad> {
            val saveLoadViewModel: SaveLoadViewModel = koinViewModel()
            SaveLoadScreen(
                viewModel = saveLoadViewModel,
                onSlotSelected = { slotId ->
                    navController.navigate(Screen.Game(slotId = slotId)) {
                        popUpTo<Screen.MainMenu>()
                    }
                },
                onBack = { navController.popBackStack() },
            )
        }
    }
}
