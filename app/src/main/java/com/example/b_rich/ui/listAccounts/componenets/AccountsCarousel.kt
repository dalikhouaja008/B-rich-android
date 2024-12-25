package com.example.b_rich.ui.listAccounts.componenets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.b_rich.data.entities.CustomAccount

@Composable
public fun AccountsCarousel(
    accounts: List<CustomAccount>,
    selectedAccount: CustomAccount?,
    onAccountSelected: (CustomAccount) -> Unit,
    currentDotIndex: Int,
    lazyListState: LazyListState
) {
    Column(
        modifier = Modifier
            .height(280.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Animation de transition pour les cartes
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .weight(1f),
            state = lazyListState,
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(accounts) { account ->
                EnhancedAccountCard(
                    account = account,
                    isSelected = selectedAccount?.id == account.id,
                    onClick = { onAccountSelected(account) }
                )
            }
        }

        // Indicateur amélioré
        DotsIndicator(
            totalDots = accounts.size,
            selectedIndex = currentDotIndex
        )
    }
}