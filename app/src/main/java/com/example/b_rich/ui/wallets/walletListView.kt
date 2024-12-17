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
    val totalBalance by viewModel.totalBalance.collectAsState()
    val hasResponse by viewModel.hasResponse.collectAsState()
    var showBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.fetchWalletsAndBalance()
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
                        text = "${String.format("%.2f", totalBalance)} TND",
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
                val newWallet = Wallet(
                    id = UUID.randomUUID().toString(),
                    userId = "current_user_id", // Replace with actual user ID
                    publicKey = generatePublicKey(),
                    privateKey = generatePrivateKey(),
                    type = "Personal",
                    network = "TND",
                    balance = amount,
                    createdAt = Date(),
                    currency = "TND",
                    originalAmount = amount,
                    convertedAmount = amount
                )

                viewModel.addWallet(newWallet)
                showBottomSheet = false
            },
            accountBalance = totalBalance
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
    LazyColumn {
        item {
            Text(
                text = "My Wallets",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        items(wallets) { wallet ->
            WalletItem(wallet)
            Divider()
        }
    }
}

@Composable
fun WalletItem(wallet: Wallet) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = wallet.type,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${String.format("%.2f", wallet.balance)} ${wallet.currency}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = wallet.network,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}


@Composable
fun BalanceCard(
    title: String,
    balance: Double,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = String.format("%.2f", balance),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

data class QuickAction(
    val icon: ImageVector,
    val label: String
)