package com.example.paintingjournal.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.paintingjournal.views.mainMenu.HomeDestination
import com.example.paintingjournal.views.mainMenu.MainMenuView
import com.example.paintingjournal.views.miniAdd.MiniAddDestination
import com.example.paintingjournal.views.miniAdd.MiniAddView
import com.example.paintingjournal.views.miniDetail.MiniDetailView
import com.example.paintingjournal.views.miniDetail.MiniatureDetailsDestination
import com.example.paintingjournal.views.miniEdit.MiniEditView
import com.example.paintingjournal.views.miniEdit.MiniatureEditDestination
import com.example.paintingjournal.views.miniList.MiniListDestination
import com.example.paintingjournal.views.miniList.MiniListView

@Composable
fun PaintingJournalNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController, 
        startDestination = HomeDestination.route, 
        modifier = Modifier ) 
    {
        composable(route = HomeDestination.route) {
            MainMenuView(
                navigateToMiniList = { navController.navigate(MiniListDestination.route) }
            )
        }

        composable(route = MiniAddDestination.route) {
            MiniAddView(
                navigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = MiniatureDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(MiniatureDetailsDestination.miniatureIdArg) {
                type = NavType.IntType
            })
            ) {
            MiniDetailView(
                navigateToEditMiniature = { navController.navigate("${MiniatureEditDestination.route}/$it") },
                navigateBack = { navController.navigateUp() }
            )
        }

        composable(
            route = MiniatureEditDestination.routeWithArgs,
            arguments = listOf(navArgument(MiniatureEditDestination.miniatureArg) {
                type = NavType.IntType
            })
        ) {
            MiniEditView(
                navigateBack = { navController.navigateUp() }
            )
        }

        composable(route = MiniListDestination.route) {
            MiniListView(
                navigateToMiniatureEntry = { navController.navigate("${MiniatureDetailsDestination.route}/${it}") },
                navigateToMiniAdd = { navController.navigate(MiniAddDestination.route) },
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}