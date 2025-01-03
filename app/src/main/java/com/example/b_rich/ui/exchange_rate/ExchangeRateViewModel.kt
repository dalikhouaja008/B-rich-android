package com.example.b_rich.ui.exchange_rate

import androidx.lifecycle.ViewModel
import com.example.b_rich.data.entities.ExchangeRate
import com.example.b_rich.data.repositories.ExchangeRateRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import com.example.b_rich.data.entities.NewsItem
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


data class ExchangeRateUiState(
    val isLoading:Boolean=false,
    val ExchangeRatesList: List<ExchangeRate>? =null,
    val errorMessage :String? =null,
)
sealed class NewsState {
    data object Loading : NewsState()
    data class Success(val news: List<NewsItem>) : NewsState()
    data class Error(val message: String) : NewsState()
}
class ExchangeRateViewModel (private val exchangeRateRepository: ExchangeRateRepository): ViewModel(){
    private val _uiState = MutableStateFlow(ExchangeRateUiState())
    val uiState: StateFlow<ExchangeRateUiState> = _uiState

    private val _newsState = MutableStateFlow<NewsState>(NewsState.Loading)
    val newsState: StateFlow<NewsState> = _newsState.asStateFlow()

    fun fetchNews() {
        viewModelScope.launch {
            _newsState.value = NewsState.Loading

            try {
                val news = exchangeRateRepository.getttingAllNews()
                println(news)
                _newsState.value = NewsState.Success(news)
            } catch (e: Exception) {
                _newsState.value = NewsState.Error(e.message ?: "Unknown error")
            }
        }
    }

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

}