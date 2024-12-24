
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.b_rich.ui.components.ExchangeRateComponents.NewsSection.NewsCarousel
import com.example.b_rich.ui.components.ExchangeRateComponents.TableauCoursDeChange.CurrencyRatesContent
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
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Section Actualités
                item {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp) // Ajout du padding horizontal
                    ) {
                        SectionTitle(
                            title = "Latest News",
                            description = "See the latest news about currencies"
                        )
                    }
                    NewsCarousel(newsUiState)
                }

                // Section des taux de change
                item {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp) // Ajout du padding horizontal
                    ) {
                        SectionTitle(
                            title = "Currency converter",
                            description = "Here you can find today's exchange rate"
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 600.dp)
                    ) {
                        CurrencyRatesContent(exchangeRateUiState)
                    }
                }
            }
        }
    }
}

