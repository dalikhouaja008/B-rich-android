package com.example.b_rich.ui.listAccounts

import CustomAccount
import ListAccountsViewModel
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ListAccountsView(
    viewModel: ListAccountsViewModel = viewModel(),
    onAddAccountClick: () -> Unit // Navigation callback for AddAccountScreen
) {
    val uiState by viewModel.uiState.collectAsState()

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(Color.White, Color(0xFF2196F3))
    )

    val selectedAccount = remember(uiState.accounts) {
        mutableStateOf(uiState.selectedAccount ?: uiState.accounts.firstOrNull())
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 72.dp), // Add padding to ensure proper spacing above the bottom bar
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            HeaderSection(
                onAddAccountClick = onAddAccountClick, // Pass the navigation callback
                onSearchQueryChange = { /* Handle search logic */ }
            )
            AccountListSection(
                accounts = uiState.accounts,
                selectedAccount = selectedAccount.value
            ) { account ->
                selectedAccount.value = account
                viewModel.selectAccount(account)
            }
            Spacer(modifier = Modifier.height(16.dp))
            AccountDetailsSection(
                selectedAccount = selectedAccount.value,
                accounts = uiState.accounts,
                viewModel = viewModel
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderSection(onAddAccountClick: () -> Unit, onSearchQueryChange: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        TextField(
            value = "",
            onValueChange = onSearchQueryChange,
            placeholder = {
                Text(
                    "Search Accounts",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)),
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Your Accounts",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.weight(1f)
            )
            FloatingActionButton(
                onClick = onAddAccountClick, // Trigger navigation to AddAccountScreen
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Account")
            }
        }
    }
}

@Composable
fun AccountListSection(
    accounts: List<CustomAccount>,
    selectedAccount: CustomAccount?,
    onAccountSelected: (CustomAccount) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(accounts) { account ->
            AccountCardView(
                account = account,
                isSelected = selectedAccount?.id == account.id,
                onClick = { onAccountSelected(account) }
            )
        }
    }
}

@Composable
fun AccountCardView(account: CustomAccount, isSelected: Boolean, onClick: () -> Unit) {
    val elevation by animateDpAsState(targetValue = if (isSelected) 8.dp else 4.dp)

    Card(
        modifier = Modifier
            .width(250.dp)
            .height(180.dp)
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF2196F3) else MaterialTheme.colorScheme.surface
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center // Center the content of the card
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally // Center text horizontally
            ) {
                Text(
                    text = account.name,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp,
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "${account.balance} TND",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

            // Bank icon remains in the top-left corner
            Icon(
                imageVector = Icons.Filled.AccountBalance,
                contentDescription = "Bank Icon",
                tint = if (isSelected) Color.White else Color.Gray,
                modifier = Modifier
                    .padding(10.dp)
                    .size(40.dp)
                    .align(Alignment.TopStart) // Keeps the icon in the top-left
            )
        }
    }
}

@Composable
fun AccountDetailsSection(
    selectedAccount: CustomAccount?,
    accounts: List<CustomAccount>,
    viewModel: ListAccountsViewModel
) {
    selectedAccount?.let { account ->
        AccountDetailsView(account, viewModel::toggleDefault)
    } ?: NoAccountSelectedMessage()
}

@Composable
fun AccountDetailsView(account: CustomAccount, onToggleDefault: (CustomAccount) -> Unit) {
    var checked by remember { mutableStateOf(account.isDefault) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
            .navigationBarsPadding() // Automatically add space above navigation bar
    ) {
        Text(
            text = "Account Details",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.1.sp
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Divider()

        Text(
            text = "Balance: ${account.balance} TND",
            style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Default Account",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = checked,
                onCheckedChange = {
                    checked = it
                    onToggleDefault(account)
                }
            )
        }

        Button(
            onClick = { /* Top-Up Logic */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                "Top-Up Wallet",
                style = MaterialTheme.typography.labelLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
fun NoAccountSelectedMessage() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "No account selected",
            style = MaterialTheme.typography.titleMedium,
            color = Color.Gray
        )
    }
}
