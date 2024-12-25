package com.example.b_rich.ui.exchange_rate.ExchangeRateComponents.TableauCoursDeChange

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
fun RateValue(
    value: String,
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End
    ) {
        Text(
            text = value,
            fontWeight = FontWeight.Bold,
            color = color,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = label,
            color = Color.Gray,
            style = MaterialTheme.typography.bodySmall
        )
    }
}