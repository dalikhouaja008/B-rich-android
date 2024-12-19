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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.b_rich.ui.wallets.WalletsViewModel
import com.example.b_rich.ui.wallets.components.CreateWalletState
import com.example.b_rich.ui.wallets.components.showNotification
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTNDWalletDialog(
    onDismiss: () -> Unit,
    onConfirm: (Double) -> Unit,
    viewModel: WalletsViewModel
) {
    var amount by remember { mutableStateOf("") }
    var isAmountError by remember { mutableStateOf(false) }
    val isLoading by viewModel.isLoading.collectAsState()
    val createWalletState by viewModel.createWalletState.collectAsState()
    val context = LocalContext.current

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
    DisposableEffect(Unit) {
        onDispose {
            viewModel.resetCreateWalletState()
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
                    text = "Create TND Wallet",
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
                // Date and Time
                Text(
                    text = "Current Date and Time (UTC):",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                        .apply { timeZone = TimeZone.getTimeZone("UTC") }
                        .format(Date()),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // User Login
                Text(
                    text = "Current User's Login:",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "raednas",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Amount Input
                OutlinedTextField(
                    value = amount,
                    onValueChange = {
                        amount = it.replace(",", ".")
                        isAmountError = try {
                            val numericAmount = amount.toDoubleOrNull() ?: 0.0
                            numericAmount <= 0
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
                        text = "Please enter a valid amount greater than 0",
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
                    val numericAmount = amount.toDoubleOrNull()
                    if (numericAmount != null && numericAmount > 0) {
                        onConfirm(numericAmount)
                    }
                },
                enabled = !isAmountError && amount.isNotBlank() && !isLoading,
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
                enabled = !isLoading
            ) {
                Text("Cancel")
            }
        },
        shape = RoundedCornerShape(16.dp),
        containerColor = Color.White,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}