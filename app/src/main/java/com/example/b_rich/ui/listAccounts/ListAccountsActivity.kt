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
import androidx.compose.material.icons.filled.Check
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
fun ListAccountsView(viewModel: ListAccountsViewModel = viewModel()) {
    // Use collectAsState to observe the state from the ViewModel
    val uiState by viewModel.uiState.collectAsState()

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFFFFFFFF), Color(0xFF2196F3))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground)
            .padding(30.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            HeaderSection(onAddAccountClick = { /* Add Account Logic */ })
            AccountListSection(uiState.accounts, uiState.selectedAccount) { account ->
                viewModel.selectAccount(account)
            }
            Spacer(modifier = Modifier.height(16.dp))
            AccountDetailsSection(uiState.selectedAccount, uiState.accounts, viewModel)
        }
    }
}

@Composable
fun HeaderSection(onAddAccountClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Your Accounts",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.weight(1f)
        )
        Button(
            onClick = onAddAccountClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2196F3), // Blue color
                contentColor = Color.White
            ),
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = "Add Account")
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
    var isExpanded by remember { mutableStateOf(false) }

    // Animate elevation with `animateDpAsState`
    val elevation by animateDpAsState(targetValue = if (isExpanded) 12.dp else 6.dp)

    // Brush for gradient background depending on selection state
    val gradientBackground = if (isSelected) {
        Brush.horizontalGradient(colors = listOf(Color(0xFF2196F3), Color(0xFF9C27B0)))
    } else {
        Brush.horizontalGradient(colors = listOf(Color.LightGray, Color.Gray))
    }

    // ElevatedCard component with animated elevation
    ElevatedCard(
        modifier = Modifier
            .width(150.dp)
            .clickable { isExpanded = !isExpanded }
            .clip(RoundedCornerShape(16.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation), // Correct way to apply elevation
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .background(gradientBackground)
                .padding(20.dp) // Increase padding here for more spacing
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Account name with bold font
                Text(
                    text = account.name,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                // Account balance with lighter font weight
                Text(
                    text = "${account.balance} TND",
                    color = Color.White,
                    fontSize = 14.sp
                )

                // Show additional info if expanded
                if (isExpanded) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Account ID: ${account.id}",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                }
            }
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
            .background(Color.White, shape = MaterialTheme.shapes.medium)
            .padding(16.dp)
    ) {
        Text("Account Details", fontWeight = FontWeight.Bold, fontSize = 16.sp)

        Spacer(modifier = Modifier.height(8.dp))

        Text("Balance: ${account.balance} TND", fontSize = 18.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Default Account", modifier = Modifier.weight(1f))

            Switch(
                checked = checked,
                onCheckedChange = {
                    checked = it
                    // Toggle default status
                    onToggleDefault(account)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                    checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                    uncheckedThumbColor = MaterialTheme.colorScheme.secondary,
                    uncheckedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                thumbContent = {
                    // Display check icon when switched on
                    if (checked) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            modifier = Modifier.size(SwitchDefaults.IconSize)
                        )
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { /* Top-Up Logic */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)), // Blue color for the button
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Top-Up Wallet", color = Color.White)
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

@Preview(showBackground = true)
@Composable
fun PreviewListAccountsView() {
    ListAccountsView()
}
