package com.example.b_rich.ui.exchange_rate.ExchangeRateComponents.NewsSection

import androidx.compose.runtime.Composable
import com.example.b_rich.ui.exchange_rate.NewsState

@Composable
fun NewsCarousel(newsState: NewsState) {
    when (newsState) {
        is NewsState.Loading -> {
            LoadingNewsCard()
        }
        is NewsState.Success -> {
            NewsCarouselContent(news = newsState.news)
        }
        is NewsState.Error -> {
            ErrorNewsCard(message = newsState.message)
        }
    }
}

