package com.example.b_rich.ui.components.ExchangeRateComponents

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.b_rich.data.entities.ExchangeRate

@Composable
fun ExchangeRateList(rates: List<ExchangeRate>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(rates) { rate ->
            ExchangeRateItem(rate)
        }
    }
}