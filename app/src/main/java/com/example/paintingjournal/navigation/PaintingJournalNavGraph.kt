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
import com.example.paintingjournal.views.paintAdd.PaintAddDestination
import com.example.paintingjournal.views.paintAdd.PaintAddView
import com.example.paintingjournal.views.paintDetail.PaintDetailView
import com.example.paintingjournal.views.paintDetail.PaintDetailsDestination
import com.example.paintingjournal.views.paintEdit.PaintEditDestination
import com.example.paintingjournal.views.paintEdit.PaintEditView
import com.example.paintingjournal.views.paintList.PaintListDestination
import com.example.paintingjournal.views.paintList.PaintListView

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
                navigateToMiniList = { navController.navigate(MiniListDestination.route) },
                navigateToPaintList = { navController.navigate(PaintListDestination.route) }
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

        composable(route = PaintAddDestination.route) {
            PaintAddView(
                navigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = PaintDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(PaintDetailsDestination.paintIdArg) {
                type = NavType.IntType
            })
        ) {
            PaintDetailView(
                navigateToEditPaint = { navController.navigate("${PaintEditDestination.route}/$it") },
                navigateBack = { navController.navigateUp() }
            )
        }

        composable(
            route = PaintEditDestination.routeWithArgs,
            arguments = listOf(navArgument(PaintEditDestination.paintArg) {
                type = NavType.IntType
            })
        ) {
            PaintEditView(navigateBack = { navController.popBackStack() })
        }

        composable(route = PaintListDestination.route) {
            PaintListView(
                navigateToPaintAdd = { navController.navigate(PaintAddDestination.route) },
                navigateBack = { navController.popBackStack() },
                navigateToPaintEntry = { navController.navigate("${PaintDetailsDestination.route}/${it}")}
            )
        }
    }
}