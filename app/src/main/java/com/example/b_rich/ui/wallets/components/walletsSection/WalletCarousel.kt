package com.example.b_rich.ui.wallets.components.walletsSection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.b_rich.data.entities.Wallet


@Composable
fun WalletsCarousel(
    wallets: List<Wallet>,
    onWalletSelected: (Wallet) -> Unit,
    selectedWallet: Wallet?
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(wallets) { wallet ->
            WalletCard(
                wallet = wallet,
                isSelected = selectedWallet == wallet,
                onSelect = { onWalletSelected(wallet) }
            )
        }
    }
}