package com.example.b_rich.ui.accounts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.b_rich.data.entities.Account
import java.util.Locale

@Composable
fun AccountScreen(
    viewModel: AccountViewModel = viewModel(),
    userId: String // Pass userId to the screen
) {
    // State variables
    var accountType by remember { mutableStateOf("Savings") }
    var balance by remember { mutableStateOf("") }
    val accounts by viewModel.accounts.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Create New Account",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Account Type Input
        OutlinedTextField(
            value = accountType,
            onValueChange = { accountType = it },
            label = { Text("Account Type") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Balance Input
        OutlinedTextField(
            value = balance,
            onValueChange = { balance = it },
            label = { Text("Initial Balance") },
            keyboardOptions = KeyboardOptions.Default,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Submit Button
        Button(
            onClick = {
                val newAccount = Account(
                    id = accounts.size + 1,
                    userId = userId,
                    type = accountType,
                    balance = balance.toDoubleOrNull() ?: 0.0,
                    status = "active"
                )
                viewModel.addAccount(newAccount)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Account")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display Existing Accounts
        Text("Existing Accounts", style = MaterialTheme.typography.titleMedium)
        accounts.forEach { account ->
            Text(
                text = "ID: ${account.id}, Type: ${account.type}, Balance: ${
                    String.format(Locale.US, "%.2f", account.balance)
                } TND",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
