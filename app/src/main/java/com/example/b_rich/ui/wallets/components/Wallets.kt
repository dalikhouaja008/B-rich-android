package com.example.b_rich.ui.wallets.components

import TransactionRow
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.b_rich.data.entities.Transaction
import com.example.b_rich.data.entities.Wallet
import com.example.b_rich.ui.currency_converter.CurrencyConverterViewModel
import com.example.b_rich.ui.wallets.WalletsViewModel
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun Wallets(
    wallets: List<Wallet>,
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
                // Section Wallets with horizontal scrolling
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        SectionTitle(
                            title = "My Wallets",
                            description = "Here you can find all your wallets and their current balances."
                        )

                        // Styled Add Wallet Button
                        IconButton(
                            onClick = {
                                //ouvrir un dialog
                            },
                            modifier = Modifier
                                .size(48.dp) // Adjusted size for better appearance
                                .clip(CircleShape)
                                .background(
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = if (wallets.size == 1 && wallets.first().currency == "TND")
                                    "Create your first wallet"
                                else
                                    "Add another wallet",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                //LazyRow for wallets
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
