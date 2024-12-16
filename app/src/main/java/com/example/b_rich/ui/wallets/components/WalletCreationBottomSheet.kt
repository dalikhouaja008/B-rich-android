package com.example.b_rich.ui.wallets.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Money
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.b_rich.ui.wallets.WalletsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletCreationBottomSheet(
    viewModel: WalletsViewModel,
    onDismiss: () -> Unit
) {
    // State variables for bank account and wallet creation
    var hasBankAccount by remember { mutableStateOf(false) }
    var initialAmount by remember { mutableStateOf("") }
    var isAmountValid by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title
            Text(
                text = "Create Your First TND Wallet",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            // Bank Account Confirmation
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccountBalance,
                        contentDescription = "Bank Account",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text("Do you have a bank account?")
                }
                Switch(
                    checked = hasBankAccount,
                    onCheckedChange = {
                        hasBankAccount = it
                        // Reset amount and error when switching
                        initialAmount = ""
                        isAmountValid = false
                        errorMessage = null
                    }
                )
            }

            // Amount Input (only visible if bank account is confirmed)
            if (hasBankAccount) {
                Column {
                    OutlinedTextField(
                        value = initialAmount,
                        onValueChange = { newValue ->
                            initialAmount = newValue
                            // Validate amount
                            val amount = newValue.toDoubleOrNull()
                            isAmountValid = amount != null && amount > 0
                            errorMessage = when {
                                newValue.isEmpty() -> "Amount cannot be empty"
                                amount == null -> "Invalid amount"
                                amount <= 0 -> "Amount must be greater than 0"
                                else -> null
                            }
                        },
                        label = { Text("Initial Amount (TND)") },
                        placeholder = { Text("Enter initial balance") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Money,
                                contentDescription = "Amount"
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = !isAmountValid && initialAmount.isNotEmpty(),
                        supportingText = {
                            errorMessage?.let {
                                Text(
                                    text = it,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Add Wallet Button
            Button(
                onClick = {
                    if (hasBankAccount && isAmountValid) {
                        val amount = initialAmount.toDoubleOrNull() ?: 0.0
                        viewModel.createFirstWallet(amount)
                        onDismiss()
                    }
                },
                enabled = hasBankAccount && isAmountValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Create Wallet")
            }

            // Informational Text
            Text(
                text = "Your first wallet will be created in Tunisian Dinars (TND)",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}