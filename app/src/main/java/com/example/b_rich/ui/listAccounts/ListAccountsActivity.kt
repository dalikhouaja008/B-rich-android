package com.example.b_rich.ui.listAccounts

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.b_rich.ui.listAccounts.componenets.AccountsCarousel
import com.example.b_rich.ui.listAccounts.componenets.AnimatedAccountDetails
import com.example.b_rich.ui.listAccounts.componenets.AnimatedHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListAccountsScreen(
    viewModel: ListAccountsViewModel,
    onAddAccountClick: () -> Unit
) {
    val accounts by viewModel.accounts.collectAsState()
    val selectedAccount by viewModel.selectedAccount.collectAsState()
    var currentDotIndex by remember { mutableStateOf(0) }
    val lazyListState = rememberLazyListState()

    // Animations et couleurs améliorées
    val primaryGradient = listOf(
        Color(0xFF6200EE),
        Color(0xFF3700B3)
    )

    val secondaryGradient = listOf(
        Color(0xFF03DAC5),
        Color(0xFF018786)
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                        )
                    )
                )
        ) {
            AnimatedHeader(
                onAddAccountClick = onAddAccountClick,
                gradientColors = primaryGradient
            )

            // Carrousel des comptes amélioré
            AccountsCarousel(
                accounts = accounts,
                selectedAccount = selectedAccount,
                onAccountSelected = { viewModel.selectAccount(it) },
                currentDotIndex = currentDotIndex,
                lazyListState = lazyListState
            )

            // Détails du compte améliorés
            AnimatedAccountDetails(
                selectedAccount = selectedAccount,
                onToggleDefault = { viewModel.toggleDefault(it) },
                gradientColors = primaryGradient,
            )
        }
    }
}












