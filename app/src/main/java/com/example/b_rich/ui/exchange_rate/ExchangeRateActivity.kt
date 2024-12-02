import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.b_rich.ui.exchange_rate.ExchangeRateViewModel
import com.example.b_rich.ui.exchange_rate.componenets.CurrencyRatesContent
import com.example.b_rich.ui.exchange_rate.componenets.NewsCarousel
import com.example.b_rich.ui.components.SectionTitle


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExchangeRate(
    exchangeRateViewModel: ExchangeRateViewModel = viewModel(),
) {
    val exchangeRateUiState by exchangeRateViewModel.uiState.collectAsState()
    val newsUiState by exchangeRateViewModel.newsState.collectAsState()

    LaunchedEffect(key1 = true) {
        exchangeRateViewModel.fetchExchangeRates()
        exchangeRateViewModel.fetchNews()
    }

    // Replace Box with a Scaffold if using Material 3
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        // Use a vertically scrollable column
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .background(Color.White.copy(alpha = 0.9f))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // News Carousel Section
            SectionTitle("Latest News")
            NewsCarousel(newsUiState)

            // Currency Rates Section
            SectionTitle("Currency Rates")
            CurrencyRatesContent(exchangeRateUiState)
        }
    }
}