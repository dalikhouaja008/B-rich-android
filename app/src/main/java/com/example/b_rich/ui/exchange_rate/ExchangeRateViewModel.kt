package com.example.b_rich.ui.exchange_rate

import androidx.lifecycle.ViewModel
import com.example.b_rich.data.entities.ExchangeRate
import com.example.b_rich.data.repositories.ExchangeRateRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

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

data class ExchangeRateUiState(
    val isLoading:Boolean=false,
    val ExchangeRatesList: List<ExchangeRate>? =null,
    val errorMessage :String? =null,
)

class ExchangeRateViewModel (private val exchangeRateRepository: ExchangeRateRepository): ViewModel(){
    private val _uiState = MutableStateFlow(ExchangeRateUiState())
    val uiState: StateFlow<ExchangeRateUiState> = _uiState

    private val _uiStateCurrency = MutableStateFlow(CurrencyUiState())
    val uiStateCurrency: StateFlow<CurrencyUiState> = _uiStateCurrency

    fun fetchExchangeRates() {
        // Mettre à jour l'état pour indiquer que le chargement a commencé
        _uiState.value = ExchangeRateUiState(isLoading = true)

        viewModelScope.launch {
            _uiState.value = ExchangeRateUiState(isLoading = true)
            try {
                // Appeler le repository pour obtenir les taux de change
                val response = exchangeRateRepository.fetchExchangeRates()

                // Vérifier si la réponse est réussie et mettre à jour l'état
                if (response.isSuccessful) {
                    response.body()?.let { rates ->
                        _uiState.value = ExchangeRateUiState(ExchangeRatesList = rates)
                    } ?: run {
                        // Gérer le cas où la réponse est vide
                        _uiState.value = ExchangeRateUiState(errorMessage = "Aucune donnée disponible")
                    }
                } else {
                    // Gérer les erreurs HTTP
                    _uiState.value = ExchangeRateUiState(errorMessage = "Erreur: ${response.code()}")
                }
            } catch (e: Exception) {
                // Gérer les exceptions (par exemple, problèmes de réseau)
                _uiState.value = ExchangeRateUiState(errorMessage = e.message)
            }
        }
    }

   /* fun formatConvertedAmount(amount: Double): String {
        val formatter = NumberFormat.getNumberInstance(Locale.getDefault())
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
    }*/
    fun toggleFromCurrencyDropdown() {
        _uiStateCurrency.update { currentState ->
            currentState.copy(
                fromCurrencyExpanded = !currentState.fromCurrencyExpanded,
                toCurrencyExpanded = false
            )
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