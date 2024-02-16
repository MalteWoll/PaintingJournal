package com.example.paintingjournal.views.miniEdit

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.paintingjournal.PaintingJournalTopAppBar
import com.example.paintingjournal.R
import com.example.paintingjournal.navigation.NavigationDestination
import com.example.paintingjournal.ui.AppViewModelProvider
import com.example.paintingjournal.views.miniAdd.MiniatureEntryBody

object MiniatureEditDestination : NavigationDestination {
    override val route = "mini_edit"
    override val titleRes = R.string.mini_edit_title
    const val miniatureArg = "miniatureId"
    val routeWithArgs = "$route/{$miniatureArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiniEditView(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MiniEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    Scaffold(
        topBar = {
            PaintingJournalTopAppBar(
                title = stringResource(MiniatureEditDestination.titleRes),
                canNavigateBack = true
            )
        },
        modifier = modifier
    ) { innerPadding ->
        MiniatureEntryBody(
            miniatureUiState = viewModel.miniatureUiState,
            onMiniatureValueChanged = { },
            onSaveClicked = { },
            modifier = Modifier.padding(innerPadding)
        )
    }
}