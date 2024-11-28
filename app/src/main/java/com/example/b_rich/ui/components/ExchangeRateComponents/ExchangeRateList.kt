package com.example.b_rich.ui.components.ExchangeRateComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.b_rich.data.entities.ExchangeRate

@Composable
fun ExchangeRateList(rates: List<ExchangeRate>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.5f))
            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
    ) {
        // Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()

                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Currency", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                Text("Buying", fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.5f), textAlign = TextAlign.End)
                Text("Selling", fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.5f), textAlign = TextAlign.End)
            }
        }

        // Rates
        items(rates) { rate ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .border(
                        width = 0.5.dp,
                        color = Color.LightGray,
                        shape = RoundedCornerShape(0.dp)
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = rate.code,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = rate.designation,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "Unit: ${rate.unit}",
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                }

                Text(
                    text = rate.buyingRate,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3D5AFE),
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(0.5f)
                )

                Text(
                    text = rate.sellingRate,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFA500),
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(0.5f)
                )
            }
        }

        // Date footer
        item {
            rates.firstOrNull()?.let { rate ->
                val dateString = rate.date
                val formattedDate = dateString.substring(0, 10) // Extraire les 10 premiers caractères (jj-mm-aaaa)
                Text(
                    text = "Date: $formattedDate",
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray.copy(alpha = 0.2f))
                        .padding(8.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp, // Taille de la police
                    fontWeight = FontWeight.Bold, // Mettre le texte en gras
                    color = Color.Gray
                )
            }
        }
    }
}