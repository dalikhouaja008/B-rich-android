package com.example.b_rich.ui.wallets.components.dialogs

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.b_rich.data.entities.Wallet
import com.example.b_rich.ui.wallets.WalletsViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.b_rich.ui.wallets.SendTransactionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendFundsDialog(
    walletsViewModel: WalletsViewModel,
    selectedWallet: Wallet,
    onDismiss: () -> Unit
) {
    var recipientAddress by remember { mutableStateOf("") }
    var sendAmount by remember { mutableStateOf("") }
    var isQRScannerOpen by remember { mutableStateOf(false) }

    val sendTransactionState by walletsViewModel.sendTransactionState.collectAsState()
    val context = LocalContext.current

    // Handle transaction result
    LaunchedEffect(sendTransactionState) {
        when (val state = sendTransactionState) {
            is SendTransactionState.Success -> {
                Toast.makeText(context, "Transaction successful!", Toast.LENGTH_SHORT).show()
                onDismiss()
            }
            is SendTransactionState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }
            else -> {} // Idle or Loading states
        }
    }

    // QR Code Scanner
    if (isQRScannerOpen) {
        QRCodeScannerDialog(
            onQRCodeScanned = { scannedAddress ->
                recipientAddress = scannedAddress
                isQRScannerOpen = false
            },
            onDismiss = { isQRScannerOpen = false }
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
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send Icon",
                    tint = Color(0xFF3D5AFE)
                )
                Text(
                    text = "Send Funds from ${selectedWallet.currency} Wallet",
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
                // Recipient Address Input
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = recipientAddress,
                        onValueChange = { recipientAddress = it },
                        label = { Text("Recipient Address") },
                        placeholder = { Text("Enter recipient's address") },
                        modifier = Modifier.weight(1f),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Recipient Icon"
                            )
                        }
                    )

                    // QR Code Scanner Button
                    IconButton(
                        onClick = { isQRScannerOpen = true },
                        modifier = Modifier
                            .background(Color(0xFF3D5AFE).copy(alpha = 0.1f), shape = CircleShape)
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCode,
                            contentDescription = "Scan QR Code",
                            tint = Color(0xFF3D5AFE)
                        )
                    }
                }

                // Amount Input
                OutlinedTextField(
                    value = sendAmount,
                    onValueChange = {
                        sendAmount = it.replace(',', '.') // Replace comma with dot
                            .filter { char -> char.isDigit() || char == '.' }
                            .let { value ->
                                // Ensure only one decimal point
                                val parts = value.split('.')
                                if (parts.size > 2)
                                    parts.first() + "." + parts.drop(1).joinToString("")
                                else
                                    value
                            }
                    },
                    label = { Text("Amount to Send") },
                    placeholder = { Text("Enter amount") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.AttachMoney,
                            contentDescription = "Amount Icon"
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                // Transaction Loading Indicator
                if (sendTransactionState is SendTransactionState.Loading) {
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
                    val amount = sendAmount.toDoubleOrNull()
                    when {
                        recipientAddress.isEmpty() -> {
                            Toast.makeText(context, "Please enter a recipient address.", Toast.LENGTH_SHORT).show()
                        }
                        amount == null || amount <= 0 -> {
                            Toast.makeText(context, "Invalid amount.", Toast.LENGTH_SHORT).show()
                        }
                        amount > selectedWallet.balance -> {
                            Toast.makeText(context, "Insufficient balance.", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            walletsViewModel.sendTransaction(
                                fromWalletPublicKey = selectedWallet.publicKey,
                                toWalletPublicKey = recipientAddress,
                                amount = amount
                            )
                        }
                    }
                },
                enabled = sendTransactionState !is SendTransactionState.Loading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3D5AFE),
                    disabledContainerColor = Color(0xFF3D5AFE).copy(alpha = 0.5f)
                )
            ) {
                Text("Send", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        shape = RoundedCornerShape(16.dp),
        containerColor = Color.White
    )
}


// Extension function for showing toast or snackbar
fun SendTransactionState.showNotification(context: Context) {
    when (this) {
        is SendTransactionState.Success -> {
            Toast.makeText(
                context,
                "Transaction successful${signature?.let { " - Signature: $it" } ?: ""}",
                Toast.LENGTH_SHORT
            ).show()
        }
        is SendTransactionState.Error -> {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
        else -> {}
    }
}
