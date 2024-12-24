package com.example.b_rich.ui.components.ExchangeRateComponents.TableauCoursDeChange

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.b_rich.data.entities.ExchangeRate

@Composable
fun RateItem(
    rate: ExchangeRate,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = animateColorAsState(
        targetValue = if (isSelected) Color(0xFF3D5AFE).copy(alpha = 0.05f) else Color.White,
        label = "backgroundColor"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .animateContentSize(),
        colors = CardDefaults.cardColors(containerColor = backgroundColor.value),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1.2f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = rate.code,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF3D5AFE),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "(${rate.unit})",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    AnimatedVisibility(visible = isSelected) {
                        Text(
                            text = rate.designation,
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                RateValue(
                    value = rate.buyingRate,
                    label = "Buy",
                    color = Color(0xFF4CAF50),
                    modifier = Modifier.weight(0.4f)
                )

                RateValue(
                    value = rate.sellingRate,
                    label = "Sell",
                    color = Color(0xFFFFA500),
                    modifier = Modifier.weight(0.4f)
                )
            }
        }
    }
}