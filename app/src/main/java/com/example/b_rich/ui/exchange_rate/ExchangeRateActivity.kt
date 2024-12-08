
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.b_rich.ui.components.ExchangeRateComponents.CurrencyRatesContent
import com.example.b_rich.ui.components.ExchangeRateComponents.NewsCarousel
import com.example.b_rich.ui.exchange_rate.ExchangeRateViewModel
import com.example.b_rich.ui.components.SectionTitle


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
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

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White.copy(alpha = 0.95f))
        ) {
            // Conteneur principal avec scrolling
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Section Actualités (scrollable)
                item {
                    SectionTitle(
                        title = "Currency converter",
                        description = "Convert for any currency you want"
                    )
                    NewsCarousel(newsUiState)
                }

                // Section des taux de change (non scrollable)
                item {
                    SectionTitle(
                        title = "Currency converter",
                        description = "Here you can find today's exchange rate"
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 600.dp) // Section fixe avec une hauteur maximale
                    ) {
                        CurrencyRatesContent(exchangeRateUiState)
                    }
                }
            }
        }
    }
}

