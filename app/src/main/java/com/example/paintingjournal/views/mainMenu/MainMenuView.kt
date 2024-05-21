package com.example.paintingjournal.views.mainMenu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.paintingjournal.PaintingJournalTopAppBar
import com.example.paintingjournal.R
import com.example.paintingjournal.navigation.NavigationDestination
import com.example.paintingjournal.ui.AppViewModelProvider

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.main_menu_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenuView(
    navigateToMiniList: () -> Unit,
    navigateToPaintList: () -> Unit,
    navigateToColorschemeList: () -> Unit,
    viewModel: MainMenuViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                PaintingJournalTopAppBar(
                    title = stringResource(id = R.string.main_menu_title),
                    canNavigateBack = false
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { navigateToMiniList() }) {
                    Text(text = stringResource(id = R.string.main_menu_mini_list_button))
                }
                Button(onClick = { navigateToPaintList() }) {
                    Text(text = stringResource(id = R.string.main_menu_paint_list_button))
                }
                Button(onClick = { navigateToColorschemeList() }) {
                    Text(text = stringResource(id = R.string.main_menu_color_scheme_list_button))
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = { viewModel.importArmyPainterFanaticRange() }) {
                    Text(text = stringResource(id = R.string.main_menu_import_army_painter_fanatic_paints))
                }
            }
        }
    }
}