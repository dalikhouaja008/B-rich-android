package com.example.b_rich.ui.exchange_rate.ExchangeRateComponents.NewsSection

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
public fun PageIndicator(
    pageCount: Int,
    currentPage: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pageCount) { iteration ->
            val color by animateColorAsState(
                targetValue = if (currentPage == iteration)
                    Color(0xFF3D5AFE)
                else
                    Color.LightGray.copy(alpha = 0.5f),
                label = "color"
            )
            val width by animateDpAsState(
                targetValue = if (currentPage == iteration) 24.dp else 8.dp,
                label = "width"
            )

            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .width(width)
                    .height(8.dp)
                    .clip(CircleShape)
                    .background(color)
                    .animateContentSize()
            )
        }
    }
}