import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.*

data class CustomAccount(
    val id: String,
    val name: String,
    val balance: Double,
    var isDefault: Boolean = false
)

// State for UI, holding accounts and selectedAccount
data class ListAccountsUiState(
    val accounts: List<CustomAccount> = emptyList(),
    val selectedAccount: CustomAccount? = null
)

class ListAccountsViewModel : ViewModel() {

    // MutableStateFlow to hold the UI state
    private val _uiState = MutableStateFlow(ListAccountsUiState())
    val uiState: StateFlow<ListAccountsUiState> = _uiState

    // Dummy data for accounts
    init {
        // In a real app, replace this with actual data fetching logic
        _uiState.value = ListAccountsUiState(
            accounts = listOf(
                CustomAccount(id = "1", name = "Account 1", balance = 100.0, isDefault = false),
                CustomAccount(id = "2", name = "Account 2", balance = 250.0, isDefault = true),
                CustomAccount(id = "3", name = "Account 3", balance = 450.0, isDefault = true),
                CustomAccount(id = "4", name = "Account 4", balance = 250.0, isDefault = true)
            )
        )
    }

    // Function to handle account selection
    fun selectAccount(account: CustomAccount) {
        _uiState.value = _uiState.value.copy(selectedAccount = account)
    }

    // Function to toggle default account status
    fun toggleDefault(account: CustomAccount) {
        _uiState.value = _uiState.value.copy(
            selectedAccount = account.copy(isDefault = !account.isDefault)
        )
    }
}
