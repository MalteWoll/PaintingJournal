package com.example.paintingjournal.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.paintingjournal.R
import com.example.paintingjournal.model.PaintingStep
import com.example.paintingjournal.views.miniAdd.ExpandablePaintingStep

@Composable
fun MiniPaintingSteps(
    onAddStep: () -> Unit,
    paintingStepList: List<ExpandablePaintingStep>,
    isEditable: Boolean,
    onToggleExpand: () -> Unit,
    onPaintingStepValueChanged: (ExpandablePaintingStep) -> Unit,
    addPaintingStep: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        if(isEditable) {
            Button(onClick = { onAddStep() }) {
                Text(
                    text = stringResource(id = R.string.mini_painting_steps_add_step)
                )
            }
        }
        PaintingStepsList(
            paintingStepList = paintingStepList,
            isEditable = isEditable,
            onToggleExpand = onToggleExpand,
            onValueChanged = onPaintingStepValueChanged
        )
        IconButton(
            onClick = { addPaintingStep() },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Icon(
                Icons.Outlined.AddCircle,
                contentDescription = "")
        }
    }
}

@Composable
fun PaintingStepsList(
    paintingStepList: List<ExpandablePaintingStep>,
    isEditable: Boolean,
    onToggleExpand: () -> Unit,
    onValueChanged: (ExpandablePaintingStep) -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        paintingStepList.forEach { paintingStep ->
            PaintingStepEntry(
                paintingStep = paintingStep,
                isEditable = isEditable,
                onToggleExpand = onToggleExpand,
                onValueChanged = onValueChanged
            )
        }
    }
}

@Composable
fun PaintingStepEntry(
    paintingStep: ExpandablePaintingStep,
    isEditable: Boolean,
    onToggleExpand: () -> Unit,
    modifier: Modifier = Modifier,
    onValueChanged: (ExpandablePaintingStep) -> Unit
) {
    Column {
        Row {
            if(isEditable && paintingStep.isExpanded) {
                OutlinedTextField(
                    value = paintingStep.stepTitle,
                    onValueChange = { onValueChanged(paintingStep.copy(stepTitle = it)) },
                    label = { stringResource(id = R.string.painting_step_title) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            } else {
                Text(
                    text = paintingStep.stepTitle,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Spacer(Modifier.weight(1f))
            IconButton(
                onClick = { onToggleExpand() },
                modifier = Modifier
                    .size(100.dp)
            ) {
                if (paintingStep.isExpanded) {
                    Icon(
                        Icons.Filled.KeyboardArrowUp,
                        contentDescription = ""
                    )
                } else {
                    Icon(
                        Icons.Filled.KeyboardArrowDown,
                        contentDescription = ""
                    )
                }
            }
        }
        if(paintingStep.isExpanded) {
            if(isEditable) {
                OutlinedTextField(
                    value = paintingStep.stepDescription,
                    onValueChange = { onValueChanged(paintingStep.copy(stepDescription = it)) },
                    label = { stringResource(id = R.string.painting_step_description) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = false
                )
            } else {
                Text(
                    text = paintingStep.stepDescription,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }

}