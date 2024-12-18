package com.example.b_rich.ui.wallets.components.dialogs

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.Money
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.b_rich.ui.currency_converter.CurrencyConverterViewModel
import com.example.b_rich.ui.wallets.WalletsViewModel
/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWalletDialog(
    availableCurrencies: List<String>,
    currencyConverterViewModel: CurrencyConverterViewModel,
    walletsViewModel: WalletsViewModel,
    onDismiss: () -> Unit,
    context: Context = LocalContext.current
) {
    var selectedCurrency by remember { mutableStateOf(availableCurrencies.firstOrNull() ?: "") }
    var dinarsAmount by remember { mutableStateOf("") }
    var convertedAmount by remember { mutableStateOf("0.0") }
    var conversionInProgress by remember { mutableStateOf(false) }
    val isLoading by walletsViewModel.isLoading.collectAsState()
    val conversionError by walletsViewModel.conversionError.collectAsState()
    val convertedWallet by walletsViewModel.convertedWallet.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    // Conversion Effect
    LaunchedEffect(dinarsAmount, selectedCurrency) {
        if (dinarsAmount.isNotEmpty() && selectedCurrency.isNotEmpty()) {
            conversionInProgress = true
            currencyConverterViewModel.calculateSellingRate(
                currency = selectedCurrency,
                amount = dinarsAmount
            )
        }
    }

    // Update converted amount
    LaunchedEffect(currencyConverterViewModel.uiStateCurrency.collectAsState().value.convertedAmount) {
        val uiState = currencyConverterViewModel.uiStateCurrency.collectAsState().value
        if (uiState.convertedAmount > 0) {
            convertedAmount = uiState.convertedAmount.toString()
            conversionInProgress = false
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.AccountBalanceWallet,
                    contentDescription = "Add Wallet Icon",
                    tint = Color(0xFF3D5AFE)
                )
                Text(
                    text = "Add New Wallet",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF3D5AFE)
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Dropdown for selecting currency
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedCurrency,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Select Currency") },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Dropdown Icon"
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Money,
                                contentDescription = "Currency Icon"
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        availableCurrencies.forEach { currency ->
                            DropdownMenuItem(
                                text = { Text(text = currency) },
                                onClick = {
                                    selectedCurrency = currency
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                // Amount in dinars
                OutlinedTextField(
                    value = dinarsAmount,
                    onValueChange = { dinarsAmount = it },
                    isError = dinarsAmount.isEmpty() && conversionInProgress,
                    label = { Text(text = "Amount in Dinars") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Money,
                            contentDescription = "Money Icon"
                        )
                    },
                    placeholder = { Text(text = "Amount in Dinars") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, shape = RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp)
                )

                // Converted amount
                OutlinedTextField(
                    value = convertedAmount,
                    onValueChange = { },
                    label = { Text(text = "Converted Amount in $selectedCurrency") },
                    readOnly = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Sync,
                            contentDescription = "Conversion Icon"
                        )
                    },
                    placeholder = { Text(text = "Converted Amount") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, shape = RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp)
                )

                if (conversionInProgress || isLoading) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFF3D5AFE)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amount = dinarsAmount.toDoubleOrNull()
                    if (amount != null && amount > 0 && selectedCurrency.isNotEmpty()) {
                        walletsViewModel.convertCurrency(
                            amount = amount,
                            fromCurrency = selectedCurrency
                        )
                    } else {
                        Toast.makeText(context, "Invalid amount or currency.", Toast.LENGTH_SHORT).show()
                    }
                },
                enabled = !isLoading && !conversionInProgress && selectedCurrency.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3D5AFE))
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White
                    )
                } else {
                    Text("Create Wallet", color = Color.White)
                }
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                border = BorderStroke(1.dp, Color(0xFF3D5AFE)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF3D5AFE)),
                enabled = !isLoading && !conversionInProgress
            ) {
                Text("Cancel")
            }
        },
        shape = RoundedCornerShape(16.dp),
        containerColor = Color.White,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}*/