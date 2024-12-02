package com.example.b_rich.ui.exchange_rate.componenets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.b_rich.ui.exchange_rate.NewsState


@Composable
fun NewsCarousel(
    newsState: NewsState
) {
    when (newsState) {
        is NewsState.Loading -> {
            CircularProgressIndicator()
        }
        is NewsState.Success -> {
            val pagerState = rememberPagerState(pageCount = { newsState.news.size })

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
            ) {
                HorizontalPager(state = pagerState) { page ->
                    val newsItem = newsState.news[page]

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, Color.Gray.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = newsItem.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                maxLines = 2
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = newsItem.content,
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 3
                            )
                        }
                    }
                }
            }

            // Page Indicators
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(newsState.news.size) { iteration ->
                    val color = if (pagerState.currentPage == iteration)
                        MaterialTheme.colorScheme.primary
                    else
                        Color.Gray

                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(8.dp)
                            .background(color, shape = CircleShape)
                    )
                }
            }
        }
        is NewsState.Error -> {
            Text(
                text = "Error loading news: ${newsState.message}",
                color = Color.Red
            )
        }
    }
}
