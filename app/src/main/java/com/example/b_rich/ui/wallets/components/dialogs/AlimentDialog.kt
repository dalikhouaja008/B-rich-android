package com.example.b_rich.ui.wallets.components.dialogs

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Money
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import com.example.b_rich.data.entities.Wallet
import com.example.b_rich.ui.currency_converter.CurrencyConverterViewModel
import com.example.b_rich.ui.currency_converter.CurrencyUiState
import com.example.b_rich.ui.wallets.WalletsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlimentDialog(
    selectedWallet: Wallet,
    uiState: CurrencyUiState,
    walletsViewModel: WalletsViewModel,
    currencyConverterViewModel: CurrencyConverterViewModel,
    onDismiss: () -> Unit,
    context: Context = LocalContext.current
) {
    var dinarsAmount by remember { mutableStateOf("") }
    var convertedAmount by remember { mutableStateOf("0.0") }
    var conversionInProgress by remember { mutableStateOf(false) }
    val isLoading by walletsViewModel.isLoading.collectAsState()
    val conversionError by walletsViewModel.conversionError.collectAsState()
    val convertedWallet by walletsViewModel.convertedWallet.collectAsState()

    // Conversion Effect
    LaunchedEffect(dinarsAmount) {
        if (dinarsAmount.isNotEmpty()) {
            conversionInProgress = true
            currencyConverterViewModel.calculateSellingRate(
                currency = selectedWallet.currency,
                amount = dinarsAmount
            )
        }
    }

    // Update converted amount
    LaunchedEffect(uiState.convertedAmount) {
        if (uiState.convertedAmount > 0) {
            convertedAmount = uiState.convertedAmount.toString()
            conversionInProgress = false
        }
    }

    // Handle conversion errors
    LaunchedEffect(conversionError) {
        conversionError?.let { error ->
            Toast.makeText(context, "Conversion failed: $error", Toast.LENGTH_LONG).show()
        }
    }

    // Handle successful conversion
    LaunchedEffect(convertedWallet) {
        convertedWallet?.let { wallet ->
            Toast.makeText(
                context,
                "Conversion successful! New balance: ${wallet.balance} ${wallet.currency}",
                Toast.LENGTH_LONG
            ).show()
            onDismiss()
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
                    contentDescription = "Wallet Icon",
                    tint = Color(0xFF3D5AFE)
                )
                Text(
                    text = "Alimenter ${selectedWallet.currency} Wallet",
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
                        .padding(2.dp)
                        .background(Color.White, shape = RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp)
                )

                // Converted amount
                OutlinedTextField(
                    value = convertedAmount,
                    onValueChange = { },
                    label = { Text(text = "Converted Amount") },
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
                        .padding(2.dp)
                        .background(Color.White, shape = RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp)
                )

                if (conversionInProgress || isLoading) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                           LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFF3D5AFE)
                )
                    }
                }

            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amount = dinarsAmount.toDoubleOrNull()
                    if (amount != null && amount > 0) {
                        walletsViewModel.convertCurrency(
                            amount = amount,
                            fromCurrency = selectedWallet.currency
                        )
                    } else {
                        Toast.makeText(context, "Invalid amount entered.", Toast.LENGTH_SHORT).show()
                    }
                },
                enabled = !isLoading && !conversionInProgress,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3D5AFE))
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White
                    )
                } else {
                    Text("Apply", color = Color.White)
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
}