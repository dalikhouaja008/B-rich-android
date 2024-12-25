package com.example.b_rich.ui.exchange_rate.ExchangeRateComponents.TableauCoursDeChange

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.b_rich.data.entities.ExchangeRate

@Composable
fun ExchangeRateList(rates: List<ExchangeRate>) {
    var selectedRate by remember { mutableStateOf<ExchangeRate?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF9F9F9))
            .padding(16.dp)
    ) {
        // Header Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Exchange Rates",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3D5AFE)
                )
                rates.firstOrNull()?.let { rate ->
                    val dateString = rate.date.substring(0, 10)
                    Text(
                        text = "Updated: $dateString",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }

        // Rates List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Column Headers
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFF3D5AFE).copy(alpha = 0.1f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Currency",
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF3D5AFE),
                        modifier = Modifier.weight(1.2f)
                    )
                    Text(
                        text = "Buy",
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF3D5AFE),
                        modifier = Modifier.weight(0.4f),
                        textAlign = TextAlign.End
                    )
                    Text(
                        text = "Sell",
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF3D5AFE),
                        modifier = Modifier.weight(0.4f),
                        textAlign = TextAlign.End
                    )
                }
            }

            // Rate Items
            items(rates) { rate ->
                RateItem(
                    rate = rate,
                    isSelected = rate == selectedRate,
                    onClick = { selectedRate = if (selectedRate == rate) null else rate }
                )
            }
        }
    }
}




