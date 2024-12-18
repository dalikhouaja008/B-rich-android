package com.example.b_rich.ui.wallets.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.b_rich.data.entities.Wallet
import com.example.b_rich.ui.currency_converter.CurrencyConverterViewModel
import com.example.b_rich.ui.wallets.WalletsViewModel
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.b_rich.ui.wallets.Wallets

@Composable
fun WalletsScreen(
    viewModel: WalletsViewModel,
    currencyConverterViewModel: CurrencyConverterViewModel
) {
    val tndWallet by viewModel.tndWallet.collectAsState()
    val currencyWallets by viewModel.currencyWallets.collectAsState()
    val recentTransactions by viewModel.recentTransactions.collectAsState()
    var selectedWallet by remember { mutableStateOf<Wallet?>(null) }
    val hasResponse by viewModel.hasResponse.collectAsState()

    LaunchedEffect(key1 = true) {
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
        when {
            tndWallet == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = {
                            // Logique pour créer un wallet TND
                        },
                        modifier = Modifier.fillMaxWidth(0.8f)
                    ) {
                        Text("Create Your First Wallet in Dinars (TND)")
                    }
                }
            }
            else -> {
                Wallets(
                    wallets = currencyWallets,
                    TNDWallet = tndWallet!!,
                    recentTransactions = recentTransactions,
                    onWalletSelected = { wallet -> selectedWallet = wallet },
                    selectedWallet = selectedWallet,
                    currencyConverterViewModel = currencyConverterViewModel,
                    viewModel = viewModel
                )
            }
        }
    }
}
