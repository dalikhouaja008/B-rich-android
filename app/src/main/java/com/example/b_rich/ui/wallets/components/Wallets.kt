package com.example.b_rich.ui.wallets.components

import TransactionRow
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.b_rich.data.entities.Transaction
import com.example.b_rich.data.entities.Wallet
import com.example.b_rich.ui.currency_converter.CurrencyConverterViewModel
import com.example.b_rich.ui.wallets.WalletsViewModel

@Composable
fun Wallets(
    wallets: List<Wallet>,
    recentTransactions: List<Transaction>,
    onWalletSelected: (Wallet) -> Unit,
    selectedWallet: Wallet?,
    currencyConverterViewModel: CurrencyConverterViewModel,
    viewModel: WalletsViewModel,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // Fond uni
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    SectionTitle(
                        title = "My Wallets",
                        description = "Here you can find all your wallets and their current balances."
                    )
                    WalletsCarousel(
                        wallets = wallets,
                        onWalletSelected = onWalletSelected,
                        selectedWallet = selectedWallet
                    )
                }
                item {
                    SectionTitle(
                        title = "Quick Actions",
                        description = "Perform actions like sending, receiving, or topping up your wallet."
                    )
                    QuickActionsRow(
                        selectedWallet = selectedWallet,
                        walletsViewModel = viewModel,
                        currencyConverterViewModel = currencyConverterViewModel
                    )
                }
                item {
                    SectionTitle(
                        title = "Recent Transactions",
                        description = "Review your latest transactions and their details here."
                    )
                }
                items(recentTransactions) { transaction ->
                    TransactionRow(transaction)
                }
            }
        }
    }
}