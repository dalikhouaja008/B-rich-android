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
import androidx.compose.ui.unit.dp
import com.example.b_rich.data.entities.Wallet
import com.example.b_rich.data.entities.Transaction
import com.example.b_rich.ui.currency_converter.CurrencyConverterViewModel
import com.example.b_rich.ui.wallets.components.QuickActionsRow
import com.example.b_rich.ui.wallets.components.SectionTitle
import com.example.b_rich.ui.wallets.components.TNDWalletCard
import com.example.b_rich.ui.wallets.components.WalletCard
import com.example.b_rich.ui.wallets.components.dialogs.AddWalletDialog

@Composable
fun Wallets(
    wallets: List<Wallet>,
    TNDWallet: Wallet,
    recentTransactions: List<Transaction>,
    onWalletSelected: (Wallet) -> Unit,
    selectedWallet: Wallet?,
    currencyConverterViewModel: CurrencyConverterViewModel,
    viewModel: WalletsViewModel,
) {
    var showDialog by remember { mutableStateOf(false) }
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
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            SectionTitle(
                                title = "My TND Wallet",
                                description = "Here you can find your TND wallet balance."
                            )

                            Button(
                                onClick = { /* Ajouter logique pour alimenter le wallet TND */ },
                                modifier = Modifier.height(40.dp),
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
                                        text = "Aliment",
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                }
                            }
                        }

                        Text(
                            text = "Your main Tunisian Dinar wallet",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )

                        TNDWalletCard(
                            wallet = TNDWallet,
                            isSelected = TNDWallet == selectedWallet,
                            onSelect = { onWalletSelected(TNDWallet) }
                        )

                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }

                // Section Wallets with horizontal scrolling
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        SectionTitle(
                            title = "My Wallets",
                            description = "Here you can find all your wallets and their current balances."
                        )

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
                                    text = if (wallets.isEmpty()) "Create Wallet" else "Add Wallet",
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        }
                    }
                }

                // LazyRow for wallets
                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
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

                // Quick Actions Section
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

                // Recent Transactions Section
                item {
                    SectionTitle(
                        title = "Recent Transactions",
                        description = "Review your latest transactions and their details here."
                    )
                }

                // Transactions List
                items(recentTransactions) { transaction ->
                    TransactionRow(transaction)
                }
            }
        }
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