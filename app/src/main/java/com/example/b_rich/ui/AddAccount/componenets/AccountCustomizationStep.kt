package com.example.b_rich.ui.AddAccount.componenets

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.b_rich.data.entities.AddAccount
import com.example.b_rich.ui.AddAccount.AddAccountViewModel

@Composable
fun AccountCustomizationStep(viewModel: AddAccountViewModel) {
    val nickname by viewModel.nickname.collectAsState()
    val accountType by viewModel.selectedAccountType.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Titre et description
        Text(
            text = "Customize Your Account",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Give your account a memorable name",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Champ de saisie du nickname
        OutlinedTextField(
            value = nickname,
            onValueChange = { viewModel.updateNickname(it) },
            label = { Text("Account Nickname") },
            placeholder = { Text("e.g. Main Account") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.AccountBox,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            )
        )

        // Suggestions de noms
        Text(
            text = "Suggested names",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(top = 8.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(getSuggestionsForType(accountType)) { suggestion ->
                SuggestionChip(
                    onClick = { viewModel.updateNickname(suggestion) },
                    label = { Text(suggestion) },
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = if (nickname == suggestion)
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f)
                    ),
                    border = SuggestionChipDefaults.suggestionChipBorder(
                        enabled = true,
                        borderColor = if (nickname == suggestion)
                            MaterialTheme.colorScheme.primary
                        else
                            Color.Transparent
                    )
                )
            }
        }

        // Texte d'aide
        Text(
            text = "Note: You can change this nickname later in account settings",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
@Composable
private fun getSuggestionsForAccount(account: AddAccount?): List<String> {
    if (account == null) return emptyList()

    val lastFourDigits = account.rib.takeLast(4)
    val currentDate = "2024" // Vous pouvez utiliser la date actuelle si nécessaire

    return listOf(
        "Main Account *$lastFourDigits",
        "Personal Account *$lastFourDigits",
        "${account.name} *$lastFourDigits",
        "Account $currentDate *$lastFourDigits",
        "My Account *$lastFourDigits"
    )
}
private fun getSuggestionsForType(accountType: String?): List<String> {
    return when (accountType?.toLowerCase()) {
        "savings" -> listOf(
            "My Savings",
            "Emergency Fund",
            "Future Projects",
            "Personal Savings",
            "Rainy Day Fund"
        )
        "checking" -> listOf(
            "Daily Expenses",
            "Main Account",
            "Personal Account",
            "Regular Use",
            "Budget Account"
        )
        "business" -> listOf(
            "Business Account",
            "Professional Use",
            "Work Account",
            "Company Funds",
            "Business Operations"
        )
        else -> listOf(
            "Main Account",
            "Personal Account",
            "Regular Account",
            "Daily Use",
            "Primary Account"
        )
    }
}