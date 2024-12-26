package com.example.b_rich.ui.wallets.components.dialogs

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
import androidx.compose.material.icons.outlined.Money
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.b_rich.data.entities.CustomAccount
import com.example.b_rich.ui.wallets.UIState
import com.example.b_rich.ui.wallets.WalletsViewModel
import com.example.b_rich.ui.wallets.components.CreateWalletState
import com.example.b_rich.ui.wallets.components.showNotification


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTNDWalletDialog(
    onDismiss: () -> Unit,
    onConfirm: (Double) -> Unit,
    viewModel: WalletsViewModel,
    hasTNDWallet: Boolean
) {
    var amount by remember { mutableStateOf("") }
    var isAmountError by remember { mutableStateOf(false) }
    val isLoading by viewModel.isLoading.collectAsState()
    val createWalletState by viewModel.createWalletState.collectAsState()
    val context = LocalContext.current
    val defaultAccountState by viewModel.defaultAccount.collectAsState()
    // Vérifier si le compte a un solde suffisant
    val currentBalance = when (defaultAccountState) {
        is UIState.Success -> (defaultAccountState as UIState.Success<CustomAccount>).data.balance ?: 0.0
        else -> 0.0
    }
    var showEmptyBalanceDialog by remember { mutableStateOf(false) }
    // Observer l'état de création et afficher les notifications
    LaunchedEffect(createWalletState) {
        when (createWalletState) {
            is CreateWalletState.Success -> {
                createWalletState.showNotification(context)
                onDismiss()
            }
            is CreateWalletState.Error -> {
                createWalletState.showNotification(context)
            }
            else -> {}
        }
    }
    LaunchedEffect(key1 = true) {
        viewModel.loadDefaultAccount()
    }
    DisposableEffect(Unit) {
        onDispose {
            viewModel.resetCreateWalletState()
        }
    }
    // Dialog pour compte vide
    if (showEmptyBalanceDialog) {
        AlertDialog(
            onDismissRequest = { showEmptyBalanceDialog = false },
            title = {
                Text(
                    text = "Insufficient Balance",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF3D5AFE)
                )
            },
            text = {
                Text(
                    text = "Your account balance is empty. Please add funds to your account before proceeding.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(
                    onClick = { showEmptyBalanceDialog = false },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFF3D5AFE)
                    )
                ) {
                    Text("OK")
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
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
                    text = if (hasTNDWallet) "Aliment your TND wallet" else "Create TND Wallet",
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
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (currentBalance > 0) Color(0xFFF5F5F5) else Color(0xFFFFEBEE)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "Available Balance",
                            style = MaterialTheme.typography.labelMedium,
                            color = if (currentBalance > 0) Color.Gray else Color.Red
                        )
                        Text(
                            text = "%.2f TND".format(currentBalance),
                            style = MaterialTheme.typography.titleMedium,
                            color = if (currentBalance > 0) Color(0xFF3D5AFE) else Color.Red,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Amount Input
                OutlinedTextField(
                    value = amount,
                    onValueChange = {
                        amount = it.replace(",", ".")
                        isAmountError = try {
                            val numericAmount = amount.toDoubleOrNull() ?: 0.0
                            numericAmount <= 0 || numericAmount > currentBalance
                        } catch (e: Exception) {
                            true
                        }
                    },
                    isError = isAmountError,
                    label = { Text("Amount in TND") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Money,
                            contentDescription = "Money Icon"
                        )
                    },
                    placeholder = { Text("Enter amount in TND") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp)
                        .background(Color.White, shape = RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp)
                )

                if (isAmountError) {
                    Text(
                        text = if ((amount.toDoubleOrNull() ?: 0.0) > currentBalance)
                            "Amount cannot exceed your available balance"
                        else
                            "Please enter a valid amount greater than 0",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                if (isLoading) {
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
                    if (currentBalance <= 0) {
                        showEmptyBalanceDialog = true
                    } else {
                        val numericAmount = amount.toDoubleOrNull()
                        if (numericAmount != null && numericAmount > 0 && numericAmount <= currentBalance) {
                            onConfirm(numericAmount)
                        }
                    }
                },
                enabled = !isAmountError &&
                        amount.isNotBlank() &&
                        !isLoading &&
                        currentBalance > 0 &&
                        (amount.toDoubleOrNull() ?: 0.0) <= currentBalance,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3D5AFE))
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White
                    )
                } else {
                    Text(
                        text = if (hasTNDWallet) "Confirm" else "Create Wallet",
                        color = Color.White
                    )
                }
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