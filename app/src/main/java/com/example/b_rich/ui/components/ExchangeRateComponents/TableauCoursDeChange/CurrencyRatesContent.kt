package com.example.b_rich.ui.components.ExchangeRateComponents.TableauCoursDeChange

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.b_rich.ui.exchange_rate.ExchangeRateUiState

@Composable
fun CurrencyRatesContent(
    uiState: ExchangeRateUiState
) {
    when {
        uiState.isLoading -> {
            CircularProgressIndicator()
        }
        uiState.errorMessage != null -> {
            Text("Error: ${uiState.errorMessage}", color = Color.Red)
        }
        else -> {
            val rates = uiState.ExchangeRatesList ?: emptyList()
            ExchangeRateList(rates)
        }
    }
}