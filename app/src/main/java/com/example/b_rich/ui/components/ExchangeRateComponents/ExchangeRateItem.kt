package com.example.b_rich.ui.components.ExchangeRateComponents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.b_rich.data.entities.ExchangeRate


@Composable
fun ExchangeRateItem(rate: ExchangeRate) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "${rate.designation} (${rate.code})", style = MaterialTheme.typography.titleLarge)
            Text(text = "Unit: ${rate.unit}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Buying Rate: ${rate.buyingRate}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Selling Rate: ${rate.sellingRate}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Date: ${rate.date}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}