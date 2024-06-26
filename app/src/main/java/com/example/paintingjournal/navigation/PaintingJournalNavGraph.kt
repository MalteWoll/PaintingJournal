package com.example.paintingjournal.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.paintingjournal.views.ColorSchemeDetails.ColorSchemeDetailsDestination
import com.example.paintingjournal.views.ColorSchemeDetails.ColorSchemeDetailsView
import com.example.paintingjournal.views.colorSchemeAdd.ColorSchemeAddDestination
import com.example.paintingjournal.views.colorSchemeAdd.ColorSchemeAddView
import com.example.paintingjournal.views.colorSchemeAddPaintList.ColorSchemeAddPaintListDestination
import com.example.paintingjournal.views.colorSchemeAddPaintList.ColorSchemeAddPaintListView
import com.example.paintingjournal.views.colorSchemeList.ColorSchemeListDestination
import com.example.paintingjournal.views.colorSchemeList.ColorSchemeListView
import com.example.paintingjournal.views.imageViewer.ImageViewerDestination
import com.example.paintingjournal.views.imageViewer.ImageViewerView
import com.example.paintingjournal.views.mainMenu.HomeDestination
import com.example.paintingjournal.views.mainMenu.MainMenuView
import com.example.paintingjournal.views.miniAdd.MiniAddDestination
import com.example.paintingjournal.views.miniAdd.MiniAddView
import com.example.paintingjournal.views.miniDetail.MiniDetailView
import com.example.paintingjournal.views.miniDetail.MiniatureDetailsDestination
import com.example.paintingjournal.views.miniEdit.MiniEditView
import com.example.paintingjournal.views.miniEdit.MiniatureEditDestination
import com.example.paintingjournal.views.miniEditPaintsList.MiniEditPaintsListView
import com.example.paintingjournal.views.miniEditPaintsList.MiniatureEditPaintsListDestination
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

@RequiresApi(Build.VERSION_CODES.P)
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
        composable(route = ColorSchemeAddDestination.route) {
            ColorSchemeAddView(
                navigateBack = { navController.popBackStack() },
                navigateToPaintList = { navController.navigate("${ColorSchemeAddPaintListDestination.route}/${it}")},
                canNavigateBack = true
            )
        }
        
        composable(
            route = ColorSchemeAddPaintListDestination.routeWithArgs,
            arguments = listOf(navArgument(ColorSchemeAddPaintListDestination.colorSchemeIdArg) {
                type = NavType.LongType
            })
        ) {
            ColorSchemeAddPaintListView(
                navigateBack = { navController.popBackStack() },
                canNavigateBack = true
            )
        }

        composable(
            route = ColorSchemeDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(ColorSchemeDetailsDestination.colorSchemeIdArg) {
                type = NavType.LongType
            })
        ) {
            ColorSchemeDetailsView(
                navigateBack = { navController.popBackStack() },
                canNavigateBack = true
            )
        }

        composable(route = ColorSchemeListDestination.route) {
            ColorSchemeListView(
                navigateToColorSchemeAdd = { navController.navigate(ColorSchemeAddDestination.route)},
                navigateBack = { navController.popBackStack() },
                navigateToColorSchemeEntry = { navController.navigate("${ColorSchemeDetailsDestination.route}/${it}") },
                canNavigateBack = true
            )
        }

        composable(
            route = ImageViewerDestination.routeWithArgs,
            arguments = listOf(
                navArgument(ImageViewerDestination.imageArg) {
                type = NavType.IntType
            },
                navArgument("entryType") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) {
            ImageViewerView(
                navigateBack = { navController.popBackStack() },
                canNavigateBack = true
            )
        }

        composable(route = HomeDestination.route) {
            MainMenuView(
                navigateToMiniList = { navController.navigate(MiniListDestination.route) },
                navigateToPaintList = { navController.navigate(PaintListDestination.route) },
                navigateToColorschemeList = { navController.navigate(ColorSchemeListDestination.route) }
            )
        }

        composable(route = MiniAddDestination.route) {
            MiniAddView(
                navigateBack = { navController.popBackStack() },
                navigateToPaintList = { navController.navigate("${MiniatureEditPaintsListDestination.route}/${it}") },
                navigateToPaintDetails = { navController.navigate("${PaintDetailsDestination.route}/${it}")},
                navigateToImageViewer = { l: Long, i: Int -> navController.navigate("${ImageViewerDestination.route}/${l}?entryType=${i}") },
                canNavigateBack = true
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
                navigateBack = { navController.navigateUp() },
                navigateToPaintDetails = { navController.navigate("${PaintDetailsDestination.route}/${it}")},
                navigateToImageViewer = { l: Long, i: Int -> navController.navigate("${ImageViewerDestination.route}/${l}?entryType=${i}") },
                canNavigateBack = true
            )
        }

        composable(
            route = MiniatureEditDestination.routeWithArgs,
            arguments = listOf(navArgument(MiniatureEditDestination.miniatureArg) {
                type = NavType.IntType
            })
        ) {
            MiniEditView(
                navigateBack = { navController.navigateUp() },
                navigateToPaintList = { navController.navigate("${MiniatureEditPaintsListDestination.route}/${it}") },
                navigateToPaintDetails = { navController.navigate("${PaintDetailsDestination.route}/${it}")},
                navigateToImageViewer = { l: Long, i: Int -> navController.navigate("${ImageViewerDestination.route}/${l}?entryType=${i}") },
                canNavigateBack = true
            )
        }

        composable(
            route = MiniatureEditPaintsListDestination.routeWithArgs,
            arguments = listOf(navArgument(MiniatureEditPaintsListDestination.miniatureArg) {
                type = NavType.IntType
            })) {
            MiniEditPaintsListView(
                navigateBack = { navController.popBackStack() },
                canNavigateBack = true,
                navigateToPaintAdd = { navController.navigate(PaintAddDestination.route) },
            )
        }

        composable(route = MiniListDestination.route) {
            MiniListView(
                navigateToMiniatureEntry = { navController.navigate("${MiniatureDetailsDestination.route}/${it}") },
                navigateToMiniAdd = { navController.navigate(MiniAddDestination.route) },
                navigateBack = { navController.popBackStack() },
                canNavigateBack = true
            )
        }

        composable(route = PaintAddDestination.route)
        {
            PaintAddView(
                navigateBack = { navController.popBackStack() },
                navigateToImageViewer = { l: Long, i: Int -> navController.navigate("${ImageViewerDestination.route}/${l}?entryType=${i}") },
                canNavigateBack = true
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
                navigateToImageViewer = { l: Long, i: Int -> navController.navigate("${ImageViewerDestination.route}/${l}?entryType=${i}") },
                navigateBack = { navController.navigateUp() },
                canNavigateBack = true
            )
        }

        composable(
            route = PaintEditDestination.routeWithArgs,
            arguments = listOf(navArgument(PaintEditDestination.paintArg) {
                type = NavType.IntType
            })
        ) {
            PaintEditView(
                navigateBack = { navController.popBackStack() },
                navigateToImageViewer = { l: Long, i: Int -> navController.navigate("${ImageViewerDestination.route}/${l}?entryType=${i}") },
                canNavigateBack = true
            )
        }

        composable(route = PaintListDestination.route) {
            PaintListView(
                navigateToPaintAdd = { navController.navigate(PaintAddDestination.route) },
                navigateBack = { navController.popBackStack() },
                navigateToPaintEntry = { navController.navigate("${PaintDetailsDestination.route}/${it}")},
                canNavigateBack = true
            )
        }
    }
}