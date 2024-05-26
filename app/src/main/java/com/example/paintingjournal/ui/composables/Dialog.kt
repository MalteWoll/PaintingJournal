package com.example.paintingjournal.ui.composables

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.paintingjournal.R

@Composable
fun DialogWithThreeButtons(
    text: String,
    buttonOneText: String,
    buttonTwoText: String,
    buttonThreeText: String,
    onButtonOneClicked: (context: Context) -> Unit,
    onButtonTwoClicked: (context: Context) -> Unit,
    onButtonThreeClicked: (context: Context) -> Unit,
    onDismissRequest: () -> Unit,
    context: Context,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small)),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = text)
                Column(horizontalAlignment = Alignment.End) {
                    Button(onClick = { onButtonOneClicked(context) }) {
                        Text(text = buttonOneText)
                    }
                    Button(onClick = { onButtonTwoClicked(context) }) {
                        Text(text = buttonTwoText)
                    }
                    Button(onClick = { onButtonThreeClicked(context) }) {
                        Text(text = buttonThreeText)
                    }
                }
            }
        }
    }
}