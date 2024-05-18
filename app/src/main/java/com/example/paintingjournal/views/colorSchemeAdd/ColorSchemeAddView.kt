package com.example.paintingjournal.views.colorSchemeAdd

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.paintingjournal.PaintingJournalTopAppBar
import com.example.paintingjournal.R
import com.example.paintingjournal.model.RgbEnum
import com.example.paintingjournal.navigation.NavigationDestination
import com.example.paintingjournal.ui.AppViewModelProvider
import com.example.paintingjournal.ui.composables.ColorPicker

object ColorSchemeAddDestination: NavigationDestination {
    override val route: String = "color_scheme_add"
    override val titleRes: Int = R.string.color_scheme_add_title
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorSchemeAddView(
    navigateBack: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: ColorSchemeAddViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    LaunchedEffect(Unit) {

    }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                PaintingJournalTopAppBar(
                    title = stringResource(id = R.string.color_scheme_add_title),
                    canNavigateBack = canNavigateBack,
                    navigateUp = navigateBack
                )
            }
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                ColorPicker(
                    onValueChanged = { i: Int, r: RgbEnum -> viewModel.onColorPickerValueChanged(i,r) },
                    colorRgb = viewModel.colorSchemeAddUiState.mainColorRgb
                )
            }
        }
    }
}