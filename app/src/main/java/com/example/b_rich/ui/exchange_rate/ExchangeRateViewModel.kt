package com.example.b_rich.ui.exchange_rate

import androidx.lifecycle.ViewModel
import com.example.b_rich.data.entities.ExchangeRate
import com.example.b_rich.data.repositories.ExchangeRateRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CurrencyUiState(
    val availableCurrencies: List<String> = listOf("TND","USD", "EUR", "GBP", "JPY","SAR"),
    var fromCurrencyExpanded: Boolean = false,
    val toCurrencyExpanded: Boolean = false,
    val convertedAmount: Double = 0.0
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

 /*   fun convertCurrency(amount: Double, fromCurrency: String, toCurrency: String) {
        viewModelScope.launch {
            try {
                //val result = exchangeRateRepository.convertCurrency(amount, fromCurrency, toCurrency)
                _uiStateCurrency.update {
                    it.copy(convertedAmount = result)
                }
            } catch (e: Exception) {
                // Handle error
            }
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
}