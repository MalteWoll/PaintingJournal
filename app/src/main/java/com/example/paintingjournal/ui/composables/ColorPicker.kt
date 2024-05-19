package com.example.paintingjournal.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.example.paintingjournal.R
import com.example.paintingjournal.model.RgbEnum

@Composable
fun ColorPicker(
    onValueChanged: (Int, RgbEnum) -> Unit,
    colorRgb: IntArray,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small))
    ) {
        ColorPickerSlider(
            title = stringResource(id = R.string.color_picker_red),
            rgbComponent = RgbEnum.RED,
            onValueChanged = onValueChanged,
            color = Color.Red,
            value = colorRgb[0]
        )
        ColorPickerSlider(
            title = stringResource(id = R.string.color_picker_green),
            rgbComponent = RgbEnum.GREEN,
            onValueChanged = onValueChanged,
            color = Color.Green,
            value = colorRgb[1]
        )
        ColorPickerSlider(
            title = stringResource(id = R.string.color_picker_blue),
            rgbComponent = RgbEnum.BLUE,
            onValueChanged = onValueChanged,
            color = Color.Blue,
            value = colorRgb[2]
        )
    }
}

@Composable
fun ColorPickerSlider(
    title: String,
    rgbComponent: RgbEnum,
    onValueChanged: (Int, RgbEnum) -> Unit,
    color: Color,
    value: Int,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        var sliderPosition by remember { mutableFloatStateOf(0f) }
        var textFieldValue by remember { mutableStateOf(TextFieldValue("")) }
        Column {
            Slider(
                value = value.toFloat(),
                onValueChange = {
                    sliderPosition = it
                    textFieldValue = TextFieldValue(sliderPosition.toInt().toString())
                    onValueChanged(it.toInt(), rgbComponent)
                },
                colors = SliderDefaults.colors(
                    thumbColor = color,
                    activeTrackColor = MaterialTheme.colorScheme.secondary,
                    inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                steps = 255,
                valueRange = 0f..255f
            )
            TextField(
                value = TextFieldValue(value.toString()),
                onValueChange = { newValue ->
                    if(newValue.text.isDigitsOnly() && newValue.text.toInt() in 0..255) {
                        textFieldValue = newValue
                        sliderPosition = newValue.text.toFloat()
                    }
                }
            )
        }
    }
}

@Composable
fun ColorSquare(
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(dimensionResource(id = R.dimen.padding_small))
        .wrapContentSize(Alignment.Center)) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(RectangleShape)
                .background(color)
        )
    }
}