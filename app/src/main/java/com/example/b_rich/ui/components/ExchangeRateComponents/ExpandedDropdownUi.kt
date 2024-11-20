package com.example.b_rich.ui.components.ExchangeRateComponents

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.PopupProperties

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpandedDropdownUi(
    label: String,
    options: List<String>,
    onSelectedItem: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by rememberSaveable { mutableStateOf("") }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { newValue ->
            expanded = newValue
        },
        modifier = modifier
    ) {
        TextField(
            value = selectedOptionText,
            onValueChange = { selectedOptionText = it },
            label = { Text(text = label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.menuAnchor()
        )

        val filteringOptions = options.filter { it.contains(selectedOptionText, ignoreCase = true) }
        if (filteringOptions.isEmpty()) return@ExposedDropdownMenuBox
        DropdownMenu(
            modifier = Modifier.exposedDropdownSize(true),
            properties = PopupProperties(focusable = false),
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            filteringOptions.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        selectedOptionText = selectionOption
                        expanded = false
                        onSelectedItem.invoke(selectedOptionText)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}