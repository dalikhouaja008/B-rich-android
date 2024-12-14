package com.example.b_rich.ui.wallets.components.dialogs

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWalletDialog(
    availableCurrencies: List<String>,
    walletBalance: String,
    onDismiss: () -> Unit,
    onAddWallet: (String, String) -> Unit
) {
    val context = LocalContext.current
    var selectedCurrency by remember { mutableStateOf(availableCurrencies.firstOrNull() ?: "") }
    var dinarsAmount by remember { mutableStateOf("") }
    var convertedAmount by remember { mutableStateOf("") }

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
                    expanded = true,
                    onExpandedChange = {}
                ) {
                    OutlinedTextField(
                        value = selectedCurrency,
                        onValueChange = { selectedCurrency = it },
                        readOnly = true,
                        label = { Text("Select Currency") },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Dropdown Icon"
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    DropdownMenu(
                        expanded = true,
                        onDismissRequest = {}
                    ) {
                        availableCurrencies.forEach { currency ->
                            DropdownMenuItem(
                                onClick = { selectedCurrency = currency },
                                text= { Text(text = currency) }
                            )
                        }
                    }
                }

                // Wallet static balance
                OutlinedTextField(
                    value = walletBalance,
                    onValueChange = {},
                    label = { Text("Wallet Balance") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Amount in dinars
                OutlinedTextField(
                    value = dinarsAmount,
                    onValueChange = { dinarsAmount = it },
                    label = { Text("Amount in Dinars") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Converted amount
                OutlinedTextField(
                    value = convertedAmount,
                    onValueChange = { convertedAmount = it },
                    label = { Text("Converted Amount") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (selectedCurrency.isNotEmpty() && dinarsAmount.isNotEmpty()) {
                        onAddWallet(selectedCurrency, dinarsAmount)
                        onDismiss()
                    } else {
                        Toast.makeText(
                            context,
                            "Please fill in all fields.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3D5AFE))
            ) {
                Text("Add Wallet", color = Color.White)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                border = BorderStroke(1.dp, Color(0xFF3D5AFE)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF3D5AFE))
            ) {
                Text("Cancel")
            }
        },
        shape = RoundedCornerShape(16.dp),
        containerColor = Color.White,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}
