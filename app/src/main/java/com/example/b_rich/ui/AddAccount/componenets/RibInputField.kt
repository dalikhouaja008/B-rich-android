package com.example.b_rich.ui.AddAccount.componenets

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RibInputField(
    rib: String,
    isValid: Boolean,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = rib,
        onValueChange = { newValue ->
            if (newValue.length <= 20 && newValue.all { it.isDigit() }) {
                onValueChange(newValue)
            }
        },
        label = { Text("RIB Number") },
        supportingText = {
            if (rib.isNotEmpty()) {
                Text(
                    text = "${rib.length}/20 digits",
                    color = if (isValid) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        },
        isError = rib.isNotEmpty() && !isValid,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.AccountBalance,
                contentDescription = null
            )
        }
    )
}