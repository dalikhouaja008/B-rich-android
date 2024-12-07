package com.example.b_rich.ui.currency_converter


import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import co.yml.charts.ui.linechart.LineChart
import com.example.b_rich.ui.components.Charts.LineChartComponent
import com.example.b_rich.ui.components.ExchangeRateComponents.ExchangeRateList
import com.example.b_rich.ui.components.ExchangeRateComponents.ExpandedDropdownUi
import com.example.b_rich.ui.components.SectionTitle
import com.example.b_rich.ui.components.TextfieldsComponenets.InputTextFieldUi
import com.example.b_rich.ui.exchange_rate.ExchangeRateViewModel
import kotlinx.coroutines.flow.take

@Composable
fun CurrencyConverter(
    currencyConverterViewModel: CurrencyConverterViewModel = viewModel()
) {
    val uiStateCurrency by currencyConverterViewModel.uiStateCurrency.collectAsState()
    var fromCurrency by remember { mutableStateOf(uiStateCurrency.fromCurrency) }
    var toCurrency by remember { mutableStateOf(uiStateCurrency.toCurrency) }
    var amount by remember { mutableStateOf(uiStateCurrency.amount) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Rendre la page scrollable
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // Ajout du scroll
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(16.dp))
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title Section
                SectionTitle("Currency Converter")

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
                        Text(
                            text = "From",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        ExpandedDropdownUi(
                            label = "Select currency",
                            selectedOption = fromCurrency,
                            options = uiStateCurrency.availableCurrencies,
                            onSelectedItem = {
                                fromCurrency = it
                                currencyConverterViewModel.updateFromCurrency(it)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Swap Icon
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .border(
                                width = 1.5.dp,
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                                shape = CircleShape
                            )
                            .clickable { currencyConverterViewModel.swapCurrencies() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.SwapHoriz,
                            contentDescription = "Swap Currencies",
                            modifier = Modifier.size(32.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    // To Currency Dropdown
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "To",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        ExpandedDropdownUi(
                            label = "Select currency",
                            selectedOption = toCurrency,
                            options = uiStateCurrency.availableCurrencies,
                            onSelectedItem = {
                                toCurrency = it
                                currencyConverterViewModel.updateToCurrency(it)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // Amount Input
                InputTextFieldUi(
                    label = "Enter Amount",
                    value = amount,
                    onValueChanged = {
                        amount = it
                        currencyConverterViewModel.updateAmount(it)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardType = KeyboardType.Decimal
                )

                // Convert Button
                Button(
                    onClick = {
                        if (uiStateCurrency.isTNDtoOtherCurrency) {
                            currencyConverterViewModel.calculateSellingRate(toCurrency, amount)
                        } else {
                            currencyConverterViewModel.calculateBuyingRate(fromCurrency, amount)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)),
                    enabled = !uiStateCurrency.isLoading
                ) {
                    if (uiStateCurrency.isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text("Convert", color = Color.White)
                    }
                }

                // Conversion Result
                when {
                    uiStateCurrency.errorMessage != null -> {
                        Text(
                            text = "Error: ${uiStateCurrency.errorMessage}",
                            color = Color.Red,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    uiStateCurrency.convertedAmount > 0 -> {
                        Text(
                            text = "Converted Amount: ${
                                currencyConverterViewModel.formatConvertedAmount(uiStateCurrency.convertedAmount)
                            } ${uiStateCurrency.toCurrency}",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color(0xFF3D5AFE)
                        )
                    }
                }

                // Prediction Section
                SectionTitle("Prediction Space")
                Text(
                    text = "You can see the variation of ${uiStateCurrency.toCurrency} over the next 7 days below.",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                LineChartComponent(
                    currencyConverterViewModel,
                    uiStateCurrency.toCurrency
                )
            }
        }
    }
}


