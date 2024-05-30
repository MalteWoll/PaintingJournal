package com.example.paintingjournal.views.ColorSchemeDetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.paintingjournal.PaintingJournalTopAppBar
import com.example.paintingjournal.R
import com.example.paintingjournal.navigation.NavigationDestination
import com.example.paintingjournal.ui.AppViewModelProvider
import com.example.paintingjournal.views.colorSchemeAdd.ColorSchemeSquares

object ColorSchemeDetailsDestination : NavigationDestination {
    override val route: String = "color_scheme_details"
    override val titleRes: Int = R.string.color_scheme_details_title
    const val colorSchemeIdArg = "colorSchemeId"
    val routeWithArgs = "$route/{$colorSchemeIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorSchemeDetailsView(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = false,
    viewModel: ColorSchemeDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    LaunchedEffect(Unit) {
        viewModel.getData()
    }

    Scaffold(
        topBar = {
            PaintingJournalTopAppBar(
                title = stringResource(R.string.color_scheme_details_title),
                canNavigateBack = canNavigateBack,
                navigateUp = navigateBack
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            ColorSchemeDetailsBody(
                colorSchemeDetailsUiState = viewModel.colorSchemeDetailsUiState
            )
        }
    }
}

@Composable
fun ColorSchemeDetailsBody(
    colorSchemeDetailsUiState: ColorSchemeDetailsUiState,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        ColorSchemeSquares(
            rgbColors = colorSchemeDetailsUiState.rgbColorsWithPaint,
            onFindClosestPaints = {  },
            canFindPaints = false
        )
    }
}