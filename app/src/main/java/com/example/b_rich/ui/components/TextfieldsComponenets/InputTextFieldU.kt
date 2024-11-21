package com.example.b_rich.ui.components.TextfieldsComponenets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputTextFieldUi(
    label: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var value by rememberSaveable { mutableStateOf("") }
    val maxDigit = 15

    TextField(

        value = value,
        onValueChange = {
            if (it.length <= maxDigit) {
                value = it
                onValueChanged.invoke(value)
            }
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        singleLine = true,
        modifier = modifier,
        supportingText = {
            Text(
                text = "${value.length} / $maxDigit",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
            )
        },




    )
}