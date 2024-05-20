package com.example.paintingjournal.views.colorSchemeAdd

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.paintingjournal.PaintingJournalTopAppBar
import com.example.paintingjournal.R
import com.example.paintingjournal.model.RgbEnum
import com.example.paintingjournal.navigation.NavigationDestination
import com.example.paintingjournal.ui.AppViewModelProvider
import com.example.paintingjournal.ui.composables.ColorPicker
import com.example.paintingjournal.ui.composables.ColorSquare

object ColorSchemeAddDestination: NavigationDestination {
    override val route: String = "color_scheme_add"
    override val titleRes: Int = R.string.color_scheme_add_title
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorSchemeAddView(
    navigateBack: () -> Unit,
    navigateToPaintList: (Long) -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: ColorSchemeAddViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    LaunchedEffect(Unit) {
        viewModel.updateMainColor()
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
            ColorSchemeAddBody(
                colorSchemeUiState = viewModel.colorSchemeAddUiState,
                onColorPickerValueChanged = { i: Int, r: RgbEnum -> viewModel.onColorPickerValueChanged(i,r) },
                toggleColorPicker = { viewModel.toggleColorPicker() },
                navigateToPaintList = { navigateToPaintList(it) },
                onSelectAnalogous = { viewModel.getAnalogousColors() },
                onSelectTriadic = { viewModel.getTriadicColors() },
                onSelectTetradic = { viewModel.getTetradicColors() },
                modifier = Modifier
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState()))
        }
    }
}

@Composable
fun ColorSchemeAddBody(
    colorSchemeUiState: ColorSchemeUiState,
    onColorPickerValueChanged: (Int, RgbEnum) -> Unit,
    toggleColorPicker: () -> Unit,
    navigateToPaintList: (Long) -> Unit,
    onSelectAnalogous: () -> Unit,
    onSelectTriadic: () -> Unit,
    onSelectTetradic: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small)),
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Button(onClick = { toggleColorPicker() }) {
                    Text(text = stringResource(id = R.string.color_scheme_add_toggle_picker))
                }
                Button(onClick = { navigateToPaintList(colorSchemeUiState.colorSchemeDetails.id) }) {
                    Text(text = stringResource(id = R.string.color_scheme_add_select_paint))
                }
            }
            if (colorSchemeUiState.showColorPicker) {
                ColorPicker(
                    onValueChanged = { i: Int, r: RgbEnum -> onColorPickerValueChanged(i, r) },
                    colorRgb = colorSchemeUiState.mainColorRgb
                )
            }
            ColorSquare(
                color = Color(
                    red = colorSchemeUiState.mainColorRgb[0],
                    green = colorSchemeUiState.mainColorRgb[1],
                    blue = colorSchemeUiState.mainColorRgb[2]
                )
            )
            ColorSchemeSelection(
                onSelectAnalogous = onSelectAnalogous,
                onSelectTriadic = onSelectTriadic,
                onSelectTetradic = onSelectTetradic,
            )
            ColorSchemeSquares(rgbColors = colorSchemeUiState.colorSchemeColors)
        }
    }
}

@Composable
fun ColorSchemeSelection(
    onSelectAnalogous: () -> Unit,
    onSelectTriadic: () -> Unit,
    onSelectTetradic: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Button(onClick = { onSelectAnalogous() }) {
                Text(text = stringResource(id = R.string.color_scheme_add_analogous_colors))
            }
            Button(onClick = { onSelectTriadic() }) {
                Text(text = stringResource(id = R.string.color_scheme_add_triadic_colors))
            }
            Button(onClick = { onSelectTetradic() }) {
                Text(text = stringResource(id = R.string.color_scheme_add_tetradic_colors))
            }
        }
    }
}

@Composable
fun ColorSchemeSquares(
    rgbColors: List<IntArray>,
    modifier: Modifier = Modifier
) {
    if(rgbColors.isNotEmpty()) {
        Column {
            rgbColors.forEach { rgbColor ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(id = R.dimen.padding_small))
                        .wrapContentSize(Alignment.CenterStart)
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RectangleShape)
                            .background(
                                Color(red = rgbColor[0], green = rgbColor[1], blue = rgbColor[2])
                            )
                    )
                }
            }
        }
    }
}