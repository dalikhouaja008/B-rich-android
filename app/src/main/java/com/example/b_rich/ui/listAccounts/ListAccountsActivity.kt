package com.example.b_rich.ui.listAccounts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Data class representing an account
data class CustomAccount(
    val id: Int,
    val name: String,
    val balance: Double,
    var isDefault: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListAccountsView() {
    // State to hold the list of accounts and the selected account
    val accounts = remember {
        mutableStateListOf(
            CustomAccount(1, "Savings Account", 1200.0, isDefault = true),
            CustomAccount(2, "Checking Account", 850.5),
            CustomAccount(3, "Investment Account", 3500.75)
        )
    }

    // Automatically select the default account if available
    var selectedAccount by remember {
        mutableStateOf(accounts.find { it.isDefault })
    }

    // Main UI structure with a Scaffold layout
    Scaffold(
        //topBar = { TopAppBarSection() }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .background(Color(0xFFF3F4F6)) // Light gray background
        ) {
            HeaderSection(onAddAccountClick = { /* Navigate to Add Account Screen */ })
            AccountListSection(accounts, selectedAccount) { account ->
                selectedAccount = account
            }
            Spacer(modifier = Modifier.height(16.dp))
            AccountDetailsSection(selectedAccount, accounts)
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
fun AccountListSection(accounts: List<CustomAccount>, selectedAccount: CustomAccount?, onAccountSelected: (CustomAccount) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        accounts.forEach { account ->
            AccountCardView(
                account = account,
                isSelected = selectedAccount?.id == account.id,
                onClick = { onAccountSelected(account) }
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

@Composable
fun AccountCardView(account: CustomAccount, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(150.dp)
            .padding(8.dp)
            .background(
                Brush.verticalGradient(
                    if (isSelected) listOf(Color(0xFF2196F3), Color(0xFF9C27B0)) // Blue to Purple gradient
                    else listOf(Color.LightGray, Color.Gray)
                ),
                shape = MaterialTheme.shapes.medium // Rounded corners for the card view
            )
            //.shadow(elevation = 4.dp, shape = MaterialTheme.shapes.medium) // Adding shadow for depth
            .padding(16.dp)
            .clickable { onClick() }
    ) {
        Text(
            text = account.name,
            color = if (isSelected) Color.White else Color.Black,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            fontSize = 16.sp // Added font size for better readability
        )

        if (isSelected) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${account.balance} TND",
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 14.sp // Added font size for consistency
            )
        }
    }
}

@Composable
fun AccountDetailsSection(selectedAccount: CustomAccount?, accounts: List<CustomAccount>) {
    selectedAccount?.let { account ->
        AccountDetailsView(account, onToggleDefault(accounts), onTopUpWallet(account))
    } ?: NoAccountSelectedMessage()
}

private fun onToggleDefault(accounts: List<CustomAccount>): (CustomAccount) -> Unit {
    return { account ->
        accounts.forEach { it.isDefault = false }
        account.isDefault = true // Set the toggled account as default
    }
}

private fun onTopUpWallet(account: CustomAccount): () -> Unit {
    return {
        println("Top-Up Wallet for ${account.name}")
    }
}

@Composable
fun AccountDetailsView(account: CustomAccount, onToggleDefault: (CustomAccount) -> Unit, onTopUpWallet: () -> Unit) {
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
                checked = account.isDefault,
                onCheckedChange = { onToggleDefault(account) },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color(0xFF2196F3), // Customize thumb color when checked
                    uncheckedThumbColor= Color.Gray // Customize thumb color when unchecked
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onTopUpWallet,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)), // Blue color for the button
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Top-Up Wallet", color = Color.White)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListAccountsViewPreview() {
    ListAccountsView()
}