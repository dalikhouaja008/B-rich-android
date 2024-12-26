package com.example.b_rich.ui.listAccounts.componenets

import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListLayoutInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.b_rich.data.entities.CustomAccount
import kotlin.math.abs


@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun AccountsCarousel(
    accounts: List<CustomAccount>,
    selectedAccount: CustomAccount?,
    onAccountSelected: (CustomAccount) -> Unit,
    lazyListState: LazyListState
) {
    // Sélectionner automatiquement le premier compte au démarrage
    LaunchedEffect(Unit) {
        if (selectedAccount == null && accounts.isNotEmpty()) {
            onAccountSelected(accounts.first())
        }
    }

    // Observer le défilement et détecter l'élément central
    LaunchedEffect(lazyListState) {
        snapshotFlow {
            val layoutInfo = lazyListState.layoutInfo
            val visibleItemsInfo = layoutInfo.visibleItemsInfo
            if (visibleItemsInfo.isEmpty()) return@snapshotFlow -1

            // Calculer le centre de la vue
            val center = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2

            // Trouver l'élément le plus proche du centre
            visibleItemsInfo.minByOrNull { abs((it.offset + it.size / 2) - center) }?.index ?: -1
        }.collect { centerIndex ->
            if (centerIndex >= 0 && centerIndex < accounts.size) {
                val centerAccount = accounts[centerIndex]
                if (selectedAccount?.id != centerAccount.id) {
                    onAccountSelected(centerAccount)
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .height(280.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .weight(1f),
            state = lazyListState,
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            flingBehavior = rememberSnapFlingBehavior(lazyListState)
        ) {
            items(
                items = accounts,
                // Utiliser un index unique au lieu de l'ID qui pourrait être null
                key = { account -> accounts.indexOf(account) }
            ) { account ->
                Box(
                    modifier = Modifier
                        .fillParentMaxWidth(0.8f)
                        .padding(horizontal = 8.dp)
                ) {
                    EnhancedAccountCard(
                        account = account,
                        isSelected = selectedAccount?.id == account.id,
                        onClick = { onAccountSelected(account) }
                    )
                }
            }
        }

        // Indicateur amélioré
        DotsIndicator(
            totalDots = accounts.size,
            selectedIndex = accounts.indexOfFirst { it.id == selectedAccount?.id }.takeIf { it >= 0 } ?: 0
        )
    }
}

// Extension function pour calculer l'index central
private fun List<LazyListItemInfo>.findCenterItemIndex(layoutInfo: LazyListLayoutInfo): Int {
    val center = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
    return minByOrNull { abs((it.offset + it.size / 2) - center) }?.index ?: -1
}