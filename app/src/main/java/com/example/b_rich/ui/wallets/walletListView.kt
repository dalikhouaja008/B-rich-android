package com.example.b_rich.ui.wallets

import TransactionRow
import androidx.collection.emptyLongSet
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.b_rich.data.entities.Wallet
import com.example.b_rich.data.entities.Transaction
import com.example.b_rich.data.entities.TransactionSolana
import com.example.b_rich.ui.currency_converter.CurrencyConverterViewModel
import com.example.b_rich.ui.wallets.components.QuickActionsRow
import com.example.b_rich.ui.wallets.components.SectionTitle
import com.example.b_rich.ui.wallets.components.TNDWalletCard
import com.example.b_rich.ui.wallets.components.TransactionsSection
import com.example.b_rich.ui.wallets.components.WalletCard
import com.example.b_rich.ui.wallets.components.dialogs.AddWalletDialogWrapper
import com.example.b_rich.ui.wallets.components.dialogs.CreateTNDWalletDialog


@Composable
fun Wallets(
    wallets: List<Wallet>,
    TNDWallet: Wallet,
    onWalletSelected: (Wallet) -> Unit,
    selectedWallet: Wallet?,
    currencyConverterViewModel: CurrencyConverterViewModel,
    viewModel: WalletsViewModel,
) {
    var showDialog by remember { mutableStateOf(false) }
    var showCreateDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                // Section TND Wallet
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                SectionTitle(
                                    title = "My TND Wallet",
                                    description = "Here you can find your TND wallet balance."
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Button(
                                onClick = { showCreateDialog=true },
                                modifier = Modifier.wrapContentWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF3D5AFE),
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(20.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        text = "Aliment TND wallet",
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                }
                            }
                        }

                        TNDWalletCard(
                            wallet = TNDWallet,
                            isSelected = TNDWallet == selectedWallet,
                            onSelect = { onWalletSelected(TNDWallet) }
                        )

                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }

                // Section Wallets
                item {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Column {
                            SectionTitle(
                                title = "My Wallets",
                                description = "Here you can find all your wallets and their current balances."
                            )

                            if (wallets.isEmpty()) {
                                // Empty state with add first wallet button
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 24.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AccountBalanceWallet,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(64.dp)
                                                .padding(bottom = 8.dp),
                                            tint = Color(0xFF3D5AFE).copy(alpha = 0.5f)
                                        )

                                        Text(
                                            text = "No Foreign Currency Wallets Yet",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = Color.Gray
                                        )

                                        Text(
                                            text = "Add your first foreign currency wallet to start managing multiple currencies",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color.Gray,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.padding(horizontal = 32.dp)
                                        )

                                        Button(
                                            onClick = { showDialog = true },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color(0xFF3D5AFE),
                                                contentColor = Color.White
                                            ),
                                            shape = RoundedCornerShape(20.dp),
                                            modifier = Modifier
                                                .padding(top = 8.dp)
                                                .height(48.dp)
                                        ) {
                                            Row(
                                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.padding(horizontal = 16.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Add,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(24.dp)
                                                )
                                                Text(
                                                    text = "Add Foreign Currency Wallet",
                                                    style = MaterialTheme.typography.labelLarge
                                                )
                                            }
                                        }
                                    }
                                }
                            } else {
                                Box(modifier = Modifier.fillMaxWidth()) {
                                    Button(
                                        onClick = { showDialog = true },
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .height(40.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF3D5AFE),
                                            contentColor = Color.White
                                        ),
                                        shape = RoundedCornerShape(20.dp),
                                        contentPadding = PaddingValues(horizontal = 16.dp)
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Add,
                                                contentDescription = null,
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Text(
                                                text = "Add Wallet",
                                                style = MaterialTheme.typography.labelMedium
                                            )
                                        }
                                    }
                                }

                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    modifier = Modifier.padding(top = 16.dp)
                                ) {
                                    items(wallets) { wallet ->
                                        WalletCard(
                                            wallet = wallet,
                                            isSelected = wallet == selectedWallet,
                                            onSelect = { onWalletSelected(wallet) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Quick Actions Section - Only show if there are foreign currency wallets
                if (wallets.isNotEmpty()) {
                    item {
                        SectionTitle(
                            title = "Quick Actions",
                            description = "Select any wallet to perform actions like sending, receiving, withdrawing or topping up your wallet."
                        )
                        QuickActionsRow(
                            selectedWallet = selectedWallet,
                            walletsViewModel = viewModel,
                            currencyConverterViewModel = currencyConverterViewModel,
                        )
                    }
                }

                // Transactions Section - Only show for selected non-TND wallet with transactions
                item {
                    selectedWallet?.let { wallet ->
                        if (wallet != TNDWallet && wallet.transactions.isNotEmpty()) {
                            TransactionsSection(
                                transactions = wallet.transactions,
                                walletPublicKey = wallet.publicKey ?: ""
                            )
                        }
                    }
                }
            }
        }
        if (showCreateDialog) {
            CreateTNDWalletDialog(
                onDismiss = { showCreateDialog = false },
                onConfirm = { amount ->
                    viewModel.createTNDWallet(amount)
                    showCreateDialog = false
                },
                viewModel = viewModel
            )
        }
        AddWalletDialogWrapper(
            showDialog = showDialog,
            availableCurrencies = listOf("EUR", "USD", "GBP"), // Vos devises disponibles
            currencyConverterViewModel = currencyConverterViewModel,
            walletsViewModel = viewModel,
            onDismiss = { showDialog = false }
        )
    }
}
data class QuickAction(
    val icon: ImageVector,
    val label: String
)
/*@Preview(showBackground = true, device = "spec:width=412dp,height=892dp", backgroundColor = 0xFF1A73E8)
@Composable
fun PreviewWallets(viewModel: WalletsViewModel = WalletsViewModel()) {
    WalletsScreen(viewModel)
}*/