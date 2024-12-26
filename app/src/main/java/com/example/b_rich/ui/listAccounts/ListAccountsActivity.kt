package com.example.b_rich.ui.listAccounts

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.b_rich.ui.AddAccount.UiState
import com.example.b_rich.ui.listAccounts.componenets.AccountsCarousel
import com.example.b_rich.ui.listAccounts.componenets.AnimatedAccountDetails
import com.example.b_rich.ui.listAccounts.componenets.AnimatedHeader
import com.example.b_rich.ui.listAccounts.componenets.EmptyAccountsState

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
    val uiState by viewModel.uiState.collectAsState()

    // Animations et couleurs améliorées
    val primaryGradient = listOf(
        Color(0xFF6200EE),
        Color(0xFF3700B3)
    )

    LaunchedEffect(key1 = true) {
        viewModel.refreshAccounts()
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface
    ) { paddingValues ->
        when (uiState) {
            is AccountsUiState.Loading -> {
                // Affichage de l'état de chargement (par exemple, un spinner)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is AccountsUiState.Error -> {
                // Affichage du message d'erreur
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = (uiState as AccountsUiState.Error).message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.refreshAccounts() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = primaryGradient[0]
                            )
                        ) {
                            Text("Réessayer")
                        }
                    }
                }
            }
            is AccountsUiState.Success -> {
                if (accounts.isEmpty()) {
                    EmptyAccountsState(
                        onAddAccountClick = onAddAccountClick,
                        gradientColors = primaryGradient
                    )
                } else {
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
                            accounts
                        )
                    }
                }
            }
        }
    }
}