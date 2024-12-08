package com.example.b_rich.ui.currency_converter

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import com.example.b_rich.data.dataModel.PredictionData
import com.example.b_rich.data.repositories.CurrencyConverterRepository
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import java.text.DecimalFormat


data class CurrencyUiState(
    val isLoading: Boolean = false,
    val availableCurrencies: List<String> = listOf("TND", "USD", "EUR", "GBP", "JPY", "SAR"),
    val fromCurrencyExpanded: Boolean = false,
    val toCurrencyExpanded: Boolean = false,
    val convertedAmount: Double = 0.0,
    val fromCurrency: String = "TND",
    val toCurrency: String = "EUR",
    val amount: String = "0.0",
    var isTNDtoOtherCurrency: Boolean = true,
    val errorMessage: String? = null
)

class CurrencyConverterViewModel(private val exchangeRateRepository: CurrencyConverterRepository): ViewModel(){


    private val _uiStateCurrency = MutableStateFlow(CurrencyUiState())
    val uiStateCurrency: StateFlow<CurrencyUiState> = _uiStateCurrency

    private val _predictions = MutableStateFlow<Map<String, List<PredictionData>>>(emptyMap())
    val predictions: StateFlow<Map<String, List<PredictionData>>> = _predictions.asStateFlow()

    private val _isLoadingPredictions = MutableStateFlow(false)
    val isLoadingPredictions: StateFlow<Boolean> = _isLoadingPredictions.asStateFlow()

    fun loadPredictions(date: String, currencies: List<String>) {
        _isLoadingPredictions.value = true
        viewModelScope.launch {
            try {
                val response = exchangeRateRepository.getCurrencyPredictions(date, currencies)
                if (response.isSuccessful) {
                    val newPredictions = response.body()?.predictions ?: emptyMap()
                    _predictions.value = newPredictions
                    Log.d("ViewModel", "Updated predictions: $newPredictions")
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("ViewModel", "Error Response: $errorBody")
                    _predictions.value = emptyMap()
                }
            } catch (e: Exception) {
                Log.e("ViewModel", "Exception in loadPredictions: ${e.message}", e)
                _predictions.value = emptyMap()
            } finally {
                _isLoadingPredictions.value = false
            }
        }
    }
    fun formatConvertedAmount(amount: Double): String {
        val formatter = NumberFormat.getNumberInstance(Locale.getDefault()) as DecimalFormat
        formatter.maximumFractionDigits = 2
        formatter.minimumFractionDigits = 0

        val formattedString = formatter.format(amount)

        // Limit to 3 digits before decimal
        val components = formattedString.split(formatter.decimalFormatSymbols.decimalSeparator)
        return if (components[0].length > 3) {
            "${components[0].take(3)}..." +
                    (if (components.size > 1) "${formatter.decimalFormatSymbols.decimalSeparator}${components[1]}" else "")
        } else {
            formattedString
        }
    }

    fun swapCurrencies() {
        // Mettez à jour l'état de manière réactive
        _uiStateCurrency.update { currentState ->
            currentState.copy(
                fromCurrency = currentState.toCurrency,
                toCurrency = currentState.fromCurrency,
                isTNDtoOtherCurrency = !currentState.isTNDtoOtherCurrency
            )
        }
    }

    fun calculateSellingRate(currency: String, amount: String) {
        viewModelScope.launch {
            _uiStateCurrency.update { it.copy(isLoading = true) }
            try {
                val result = exchangeRateRepository.getSellingRate(currency, amount)
                _uiStateCurrency.update {
                    it.copy(
                        convertedAmount = result,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiStateCurrency.update {
                    it.copy(
                        errorMessage = e.localizedMessage,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun calculateBuyingRate(currency: String, amount: String) {
        viewModelScope.launch {
            _uiStateCurrency.update { it.copy(isLoading = true) }
            try {
                val result = exchangeRateRepository.getBuyingRate(currency, amount)
                _uiStateCurrency.update {
                    it.copy(
                        convertedAmount = result,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiStateCurrency.update {
                    it.copy(
                        errorMessage = e.localizedMessage,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun updateFromCurrency(currency: String) {
        _uiStateCurrency.update {
            it.copy(
                fromCurrency = currency,
                isTNDtoOtherCurrency = currency == "TND"
            )
        }
    }

    fun updateToCurrency(currency: String) {
        _uiStateCurrency.update {
            it.copy(
                toCurrency = currency,
            )
        }
    }

    fun updateAmount(amount: String) {
        _uiStateCurrency.update { it.copy(amount = amount) }
    }
}