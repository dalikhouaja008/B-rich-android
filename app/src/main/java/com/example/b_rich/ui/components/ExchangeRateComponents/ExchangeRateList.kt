package com.example.b_rich.ui.components.ExchangeRateComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
            .heightIn(max = 600.dp) // Hauteur maximale augmentée
            .fillMaxWidth()
            .background(Color(0xFFF9F9F9))
            .border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(8.dp))
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // En-tête
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Currency",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3D5AFE),
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Buying",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3D5AFE),
                    modifier = Modifier.weight(0.5f),
                    textAlign = TextAlign.End
                )
                Text(
                    text = "Selling",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3D5AFE),
                    modifier = Modifier.weight(0.5f),
                    textAlign = TextAlign.End
                )
            }
        }

        // Liste des taux
        items(rates) { rate ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp)) // Coins arrondis
                    .background(Color.White) // Fond blanc pour chaque item
                    .border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(8.dp)) // Bordure subtile
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = rate.code,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color(0xFF3D5AFE)
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
                    fontSize = 14.sp,
                    color = Color(0xFF3D5AFE),
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(0.5f)
                )

                Text(
                    text = rate.sellingRate,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFFFFA500),
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(0.5f)
                )
            }
        }

        // Pied de page avec la date
        item {
            rates.firstOrNull()?.let { rate ->
                val dateString = rate.date
                val formattedDate = dateString.substring(0, 10) // Format jj-mm-aaaa
                Text(
                    text = "Date: $formattedDate",
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFEEEEEE)) // Fond gris clair pour le pied de page
                        .padding(8.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp, // Taille du texte réduite
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3D5AFE)
                )
            }
        }
    }
}
