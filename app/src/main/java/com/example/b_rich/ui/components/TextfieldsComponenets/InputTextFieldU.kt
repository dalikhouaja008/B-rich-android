package com.example.b_rich.ui.components.TextfieldsComponenets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputTextFieldUi(
    label: String,
    value:String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Number
) {
    var text by remember { mutableStateOf("") }

    OutlinedTextField(
        value = value,
        onValueChange = {
            text = it
            onValueChanged(it)
        },
        label = { Text(label) },
        isError = isError,
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.AttachMoney,
                contentDescription = "$label Input Icon"
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        singleLine = true
    )
}