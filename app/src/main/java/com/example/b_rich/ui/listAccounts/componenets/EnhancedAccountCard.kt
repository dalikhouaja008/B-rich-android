package com.example.b_rich.ui.listAccounts.componenets

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.b_rich.data.entities.CustomAccount

@Composable
public fun EnhancedAccountCard(
    account: CustomAccount,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val cornerRadius = 24.dp
    val elevation by animateDpAsState(
        targetValue = if (isSelected) 12.dp else 4.dp,
        label = "card elevation"
    )

    Card(
        modifier = Modifier
            .width(200.dp)
            .height(160.dp)
            .shadow(
                elevation = elevation,
                shape = RoundedCornerShape(cornerRadius),
                spotColor = Color(0xFF9C27B0).copy(alpha = 0.3f)
            )
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(cornerRadius),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        if (isSelected) {
                            listOf(Color(0xFF6200EE), Color(0xFF3700B3))
                        } else {
                            listOf(Color(0xFF9C27B0), Color(0xFF6200EE))
                        }
                    )
                )
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top section with icon and nickname
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Default.AccountBalance,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.size(32.dp)
                    )

                    // Display nickname with RIB fallback
                    Text(
                        text = account.nickname ?: "Account ${account.rib.takeLast(4)}",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }

                // Bottom section with RIB and balance
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Display masked RIB
                    Text(
                        text = "RIB: ****${account.rib.takeLast(4)}",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )

                    // Display balance
                    Text(
                        text = "${account.balance ?: 0.0} TND",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )

                    // Default account indicator if applicable
                    if (account.isDefault == true) {
                        Text(
                            text = "Default Account",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }
}