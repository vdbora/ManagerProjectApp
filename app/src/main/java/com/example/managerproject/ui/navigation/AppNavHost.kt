package com.example.managerproject.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.managerproject.ui.boards.BoardsScreen
import com.example.managerproject.ui.navigation.AppDestinations.Args
import com.example.managerproject.ui.screens.TempAction
import com.example.managerproject.ui.screens.TemporaryScreen

private const val ANIM_MS = 300

/**
 * The navigation graph: maps every route to its screen.
 *
 * - NavController performs navigate() / popBackStack().
 * - NavHost shows the composable of the current route.
 * - Screens never receive the NavController; they get simple callbacks
 *   (onOpenBoard, onBack, ...), which keeps them reusable and easy to test.
 */
@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = AppDestinations.BOARDS,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
        // Forward: new screen slides in from the right. Back: it slides out to the right.
        enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(ANIM_MS)) },
        exitTransition = { fadeOut(tween(ANIM_MS)) },
        popEnterTransition = { fadeIn(tween(ANIM_MS)) },
        popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(ANIM_MS)) },
    ) {

        // ---------- Onboarding ----------
        composable(AppDestinations.ONBOARDING) {
            TemporaryScreen(
                title = "Welcome",
                description = "Onboarding (first launch only).",
                actions = listOf(
                    TempAction("Get started") {
                        // Clear the whole back stack: Boards becomes the only screen,
                        // so system Back from Boards closes the app instead of reopening onboarding
                        navController.navigate(AppDestinations.BOARDS) {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    }
                ),
            )
        }

        // ---------- Boards (home) ----------
        composable(AppDestinations.BOARDS) {
            BoardsScreen(
                onOpenBoard = { boardId -> navController.navigate(AppDestinations.board(boardId)) },
                onOpenSettings = { navController.navigate(AppDestinations.SETTINGS) },
            )
        }

        // ---------- Board ----------
        composable(
            route = AppDestinations.BOARD,
            arguments = listOf(navArgument(Args.BOARD_ID) { type = NavType.LongType }),
        ) { entry ->
            val boardId = entry.arguments?.getLong(Args.BOARD_ID) ?: AppDestinations.NO_ID
            TemporaryScreen(
                title = "Board $boardId",
                description = "Columns and cards of board $boardId.",
                actions = listOf(
                    TempAction("Open card 1") { navController.navigate(AppDestinations.cardDetail(1)) },
                    TempAction("Add card to column 1") { navController.navigate(AppDestinations.newCard(boardId, 1)) },
                ),
                onNavigateUp = { navController.popBackStack() },
            )
        }

        // ---------- Card detail ----------
        composable(
            route = AppDestinations.CARD_DETAIL,
            arguments = listOf(navArgument(Args.CARD_ID) { type = NavType.LongType }),
        ) { entry ->
            val cardId = entry.arguments?.getLong(Args.CARD_ID) ?: AppDestinations.NO_ID
            TemporaryScreen(
                title = "Card $cardId",
                description = "Details of card $cardId.",
                actions = listOf(TempAction("Edit") { navController.navigate(AppDestinations.editCard(cardId)) }),
                onNavigateUp = { navController.popBackStack() },
            )
        }

        // ---------- Add / edit card ----------
        composable(
            route = AppDestinations.CARD_EDIT,
            arguments = listOf(
                navArgument(Args.BOARD_ID) { type = NavType.LongType; defaultValue = AppDestinations.NO_ID },
                navArgument(Args.COLUMN_ID) { type = NavType.LongType; defaultValue = AppDestinations.NO_ID },
                navArgument(Args.CARD_ID) { type = NavType.LongType; defaultValue = AppDestinations.NO_ID },
            ),
        ) { entry ->
            val cardId = entry.arguments?.getLong(Args.CARD_ID) ?: AppDestinations.NO_ID
            val columnId = entry.arguments?.getLong(Args.COLUMN_ID) ?: AppDestinations.NO_ID
            // The Camera screen puts the photo path here before it closes
            val photoPath by entry.savedStateHandle
                .getStateFlow<String?>(Args.PHOTO_PATH, null)
                .collectAsStateWithLifecycle()
            TemporaryScreen(
                title = if (cardId == AppDestinations.NO_ID) "New card" else "Edit card $cardId",
                description = "Column: $columnId\nPhoto: ${photoPath ?: "none"}",
                actions = listOf(
                    TempAction("Save") { navController.popBackStack() },
                    TempAction("Take photo") { navController.navigate(AppDestinations.CAMERA) },
                    TempAction("Cancel") { navController.popBackStack() },
                ),
                onNavigateUp = { navController.popBackStack() },
            )
        }

        // ---------- Camera ----------
        composable(AppDestinations.CAMERA) {
            TemporaryScreen(
                title = "Camera",
                description = "CameraX preview and capture.",
                actions = listOf(
                    TempAction("Use photo") {
                        // Send the result back to the screen below (the form), then close
                        navController.previousBackStackEntry?.savedStateHandle?.set(Args.PHOTO_PATH, "/test/photo.jpg")
                        navController.popBackStack()
                    }
                ),
                onNavigateUp = { navController.popBackStack() },
            )
        }

        // ---------- Settings ----------
        composable(AppDestinations.SETTINGS) {
            TemporaryScreen(
                title = "Settings",
                description = "Theme, sensors and reminders.",
                actions = listOf(TempAction("Show onboarding again") { navController.navigate(AppDestinations.ONBOARDING) }),
                onNavigateUp = { navController.popBackStack() },
            )
        }
    }
}
