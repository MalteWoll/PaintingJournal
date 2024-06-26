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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.paintingjournal.model.ColorSchemeEnum
import com.example.paintingjournal.model.RgbColorWithPaint
import com.example.paintingjournal.model.RgbEnum
import com.example.paintingjournal.navigation.NavigationDestination
import com.example.paintingjournal.ui.AppViewModelProvider
import com.example.paintingjournal.ui.composables.ColorPicker
import com.example.paintingjournal.ui.composables.ColorSquare
import com.example.paintingjournal.views.paintAdd.toPaint
import com.example.paintingjournal.views.paintList.PaintItem

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
                onFindClosestPaints = { viewModel.findClosestPaints() },
                onAddColorScheme = { viewModel.onAddColorScheme() },
                onValueChanged = viewModel::updateUiState,
                navigateBack = navigateBack,
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
    onFindClosestPaints: () -> Unit,
    onAddColorScheme: () -> Unit,
    onValueChanged: (ColorSchemeDetails) -> Unit,
    navigateBack: () -> Unit,
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
            ColorSchemeSquares(
                rgbColors = colorSchemeUiState.colorSchemeColors,
                onFindClosestPaints = onFindClosestPaints
            )
            if(colorSchemeUiState.selectedColorScheme != ColorSchemeEnum.NONE) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = colorSchemeUiState.colorSchemeDetails.name,
                        onValueChange = { onValueChanged(colorSchemeUiState.colorSchemeDetails.copy(name = it)) },
                        label = { Text(stringResource(id = R.string.mini_add_form_name)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                            unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                            disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        enabled = true,
                        singleLine = true
                    )
                    Button(onClick = {
                        onAddColorScheme()
                        navigateBack()
                    }) {
                        Text(text = stringResource(id = R.string.color_scheme_add_button_add_scheme))
                    }
                }
            }
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
    rgbColors: List<RgbColorWithPaint>,
    onFindClosestPaints: () -> Unit,
    modifier: Modifier = Modifier,
    canFindPaints: Boolean = true,
) {
    if(rgbColors.isNotEmpty()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            rgbColors.forEach { rgbColor ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(dimensionResource(id = R.dimen.padding_small))
                            .wrapContentSize(Alignment.CenterStart)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(RectangleShape)
                                    .background(
                                        Color(
                                            red = rgbColor.rgbColor[0],
                                            green = rgbColor.rgbColor[1],
                                            blue = rgbColor.rgbColor[2]
                                        )
                                    )
                            )
                            if(rgbColor.miniaturePaint.hexColor != "") {
                                PaintItem(
                                    paint = rgbColor.miniaturePaint.toPaint(),
                                )
                            }
                        }
                    }
                }
            }
            if(canFindPaints) {
                Button(onClick = { onFindClosestPaints() }) {
                    Text(text = stringResource(id = R.string.color_scheme_add_find_closest_paints))
                }
            }
        }
    }
}