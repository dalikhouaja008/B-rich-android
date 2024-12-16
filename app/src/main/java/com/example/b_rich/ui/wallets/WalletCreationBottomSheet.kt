package com.example.b_rich.ui.wallets

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletCreationBottomSheet(
    onDismiss: () -> Unit,
    onDone: (Double) -> Unit,
    accountBalance: Double
) {
    var amount by remember { mutableStateOf("") }
    var openBottomSheet by remember { mutableStateOf(true) }
    var isAmountError by remember { mutableStateOf(false) }

    if (openBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                openBottomSheet = false
                onDismiss()
            },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                // Display total account balance
                Text(
                    text = "Your Total Account Balance: ${String.format("%.2f", accountBalance)} TND",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Amount input field with validation
                TextField(
                    value = amount,
                    onValueChange = {
                        amount = it.replace(",", ".")
                        // Validate input
                        isAmountError = try {
                            val numericAmount = amount.toDoubleOrNull() ?: 0.0
                            numericAmount > accountBalance || numericAmount < 0
                        } catch (e: Exception) {
                            true
                        }
                    },
                    label = { Text("Enter Wallet Amount (TND)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = isAmountError,
                    supportingText = {
                        if (isAmountError) {
                            Text(
                                text = "Amount must be between 0 and ${"%.2f".format(accountBalance)} TND",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Button row
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            openBottomSheet = false
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            val numericAmount = amount.toDoubleOrNull() ?: 0.0
                            if (numericAmount > 0 && numericAmount <= accountBalance) {
                                openBottomSheet = false
                                onDone(numericAmount)
                            } else {
                                isAmountError = true
                            }
                        },
                        enabled = !isAmountError && amount.isNotBlank()
                    ) {
                        Text("Create Wallet")
                    }
                }
            }
        }
    }
}