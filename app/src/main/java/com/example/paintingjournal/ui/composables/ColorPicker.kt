package com.example.paintingjournal.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
    Column {
        ColorPickerSlider(
            title = stringResource(id = R.string.color_picker_red),
            rgbComponent = RgbEnum.RED,
            onValueChanged = onValueChanged
        )
        ColorPickerSlider(
            title = stringResource(id = R.string.color_picker_blue),
            rgbComponent = RgbEnum.BLUE,
            onValueChanged = onValueChanged
        )
        ColorPickerSlider(
            title = stringResource(id = R.string.color_picker_green),
            rgbComponent = RgbEnum.GREEN,
            onValueChanged = onValueChanged
        )
        ColorSquare(color = Color(red = colorRgb[0], blue = colorRgb[1], green = colorRgb[2]))
    }
}

@Composable
fun ColorPickerSlider(
    title: String,
    rgbComponent: RgbEnum,
    onValueChanged: (Int, RgbEnum) -> Unit,
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
                value = sliderPosition,
                onValueChange = {
                    sliderPosition = it
                    onValueChanged(it.toInt(), rgbComponent)
                },
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.secondary,
                    activeTrackColor = MaterialTheme.colorScheme.secondary,
                    inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                steps = 1,
                valueRange = 0f..255f
            )
            TextField(
                value = textFieldValue,
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
        .wrapContentSize(Alignment.Center)) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(RectangleShape)
                .background(color)
        )
    }
}