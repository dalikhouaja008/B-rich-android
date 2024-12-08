package com.example.b_rich.ui.wallets.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.b_rich.data.entities.Wallet

@Composable
fun WalletCard(
    wallet: Wallet,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val elevation by animateDpAsState(if (isSelected) 12.dp else 4.dp)
    val borderColor = if (isSelected) Color(0xFF3D5AFE) else Color.Transparent

    // Assign gradient colors for each currency
    val gradientBackground = when (wallet.currency) {
        "Tunisian Dinar" -> Brush.horizontalGradient(
            colors = listOf(Color(0xFFFFD700), Color(0xFFFFE135))
        )
        "Euro" -> Brush.horizontalGradient(
            colors = listOf(Color(0xFF1976D2), Color(0xFF90CAF9))
        )
        "US Dollar" -> Brush.horizontalGradient(
            colors = listOf(Color(0xFF4CAF50), Color(0xFF81C784))
        )
        else -> Brush.horizontalGradient(
            colors = listOf(Color.Gray, Color.LightGray)
        )
    }

    Card(
        modifier = Modifier
            .width(280.dp)
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onSelect() }
            .border(2.dp, borderColor, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(elevation),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .background(gradientBackground)
                .padding(20.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountBalanceWallet,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                    Text(
                        text = wallet.currency,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "${wallet.balance} SOL",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Text(
                        text = "Original: ${"%.2f".format(wallet.originalAmount.toDouble())} ${wallet.currency}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Normal,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .background(Color.White.copy(alpha = 0.8f))
                    )
                }
            }
        }
    }
}