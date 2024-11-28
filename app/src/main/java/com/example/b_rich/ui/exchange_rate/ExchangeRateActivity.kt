
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.b_rich.data.entities.user
import com.example.b_rich.ui.resetPassword.ResetPasswordViewModel
import com.example.b_rich.ui.theme.PREF_FILE
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.input.KeyboardType
import com.example.b_rich.ui.components.ExchangeRateComponents.ExchangeRateList
import com.example.b_rich.ui.components.ExchangeRateComponents.ExpandedDropdownUi
import com.example.b_rich.ui.components.TextfieldsComponenets.InputTextFieldUi
import com.example.b_rich.ui.exchange_rate.ExchangeRateViewModel
import kotlinx.coroutines.flow.take

@Composable
fun ExchangeRate(

    exchangeRateViewModel: ExchangeRateViewModel = viewModel()
) {
    val uiState by exchangeRateViewModel.uiState.collectAsState()

    LaunchedEffect(key1 = true) {
        exchangeRateViewModel.fetchExchangeRates()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()

            .padding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.White.copy(alpha = 0.9f), shape = MaterialTheme.shapes.medium)
                .padding(24.dp),

            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Currency Conversion Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // From Currency Dropdown
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Exchange Rates Section
                    when {
                        uiState.isLoading -> {
                            CircularProgressIndicator()
                        }

                        uiState.errorMessage != null -> {
                            Text("Error: ${uiState.errorMessage}", color = Color.Red)
                        }

                        else -> {
                            val rates = uiState.ExchangeRatesList ?: emptyList()
                            ExchangeRateList(rates)
                        }
                    }
                }
            }
        }
    }
}
