package com.example.b_rich.ui.wallets

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.b_rich.data.entities.Wallet
import com.example.b_rich.data.entities.Transaction
import com.example.b_rich.ui.currency_converter.CurrencyConverterViewModel
import java.util.Date
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletsScreen(
    viewModel: WalletsViewModel,
    currencyConverterViewModel: CurrencyConverterViewModel
) {
    val wallets by viewModel.wallets.collectAsState()
    val recentTransactions by viewModel.recentTransactions.collectAsState()
    val hasResponse by viewModel.hasResponse.collectAsState()
    var showBottomSheet by remember { mutableStateOf(false) }

    // Calculate total account balance in TND
    val accountBalance = wallets
        .filter { it.currency == "TND" }
        .sumOf { it.balance }

    LaunchedEffect(Unit) {
        viewModel.fetchWallets()
    }

    if (!hasResponse) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Display total account balance
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Total Account Balance",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "${String.format("%.2f", accountBalance)} TND",
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
            }

            // Create Wallet Button
            Button(
                onClick = { showBottomSheet = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Create New Wallet in Dinars (TND)")
            }

            // Wallet List or Empty State
            when {
                wallets.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No wallets found. Create your first wallet!")
                    }
                }
                else -> {
                    // Display existing wallets list
                    // You can add your wallet list UI here
                    WalletsList(wallets)
                }
            }
        }
    }

    // Bottom Sheet for Wallet Creation
    if (showBottomSheet) {
        WalletCreationBottomSheet(
            onDismiss = { showBottomSheet = false },
            onDone = { amount ->
                // Create a new wallet with all required fields
                val newWallet = Wallet(
                    id = UUID.randomUUID().toString(), // Generate unique ID
                    userId = "current_user_id", // Replace with actual user ID
                    publicKey = generatePublicKey(), // Implement public key generation
                    privateKey = generatePrivateKey(), // Implement private key generation
                    type = "Personal",
                    network = "TND", // Assuming TND is the network
                    balance = amount,
                    createdAt = Date(), // Use current date
                    currency = "TND",
                    originalAmount = amount,
                    convertedAmount = amount
                )

                viewModel.addWallet(newWallet)
                showBottomSheet = false
            },
            accountBalance = accountBalance
        )
    }
}

// Helper function to generate public key (implement actual generation logic)
private fun generatePublicKey(): String {
    // Replace with actual public key generation logic
    return "PUB_" + UUID.randomUUID().toString()
}

// Helper function to generate private key (implement actual generation logic)
private fun generatePrivateKey(): String {
    // Replace with actual private key generation logic
    return "PRIV_" + UUID.randomUUID().toString()
}

@Composable
fun WalletsList(wallets: List<Wallet>) {
    // Implement a list or grid of wallets
    Column {
        wallets.forEach { wallet ->
            WalletItem(wallet)
        }
    }
}

@Composable
fun WalletItem(wallet: Wallet) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Wallet Type: ${wallet.type}")
            Text("Network: ${wallet.network}")
            Text("Balance: ${String.format("%.2f", wallet.balance)} ${wallet.currency}")
            Text("Created: ${wallet.createdAt}")
        }
    }
}

data class QuickAction(
    val icon: ImageVector,
    val label: String
)