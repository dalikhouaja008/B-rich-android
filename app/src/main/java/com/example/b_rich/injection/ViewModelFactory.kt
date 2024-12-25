package com.example.b_rich.injection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.b_rich.data.network.ApiService
import com.example.b_rich.data.repositories.AccountRepository
import com.example.b_rich.data.repositories.CurrencyConverterRepository
import com.example.b_rich.data.repositories.ExchangeRateRepository
import com.example.b_rich.data.repositories.UserRepository
import com.example.b_rich.data.repositories.WalletRepository
import com.example.b_rich.ui.AddAccount.AddAccountViewModel
import com.example.b_rich.ui.currency_converter.CurrencyConverterViewModel
import com.example.b_rich.ui.exchange_rate.ExchangeRateViewModel
import com.example.b_rich.ui.listAccounts.ListAccountsViewModel
import com.example.b_rich.ui.resetPassword.ResetPasswordViewModel
import com.example.b_rich.ui.signin.SigninViewModel
import com.example.b_rich.ui.signup.SignupViewModel
import com.example.b_rich.ui.wallets.WalletsViewModel

//Cette approche vous permet de centraliser la création de vos ViewModels tout en gardant la flexibilité d'ajouter de nouveaux ViewModels
// facilement.
class ViewModelFactory(

    private val userRepository: UserRepository,
    private val exchangeRateRepository: ExchangeRateRepository,
    private val currencyRepository: CurrencyConverterRepository,
    private val walletRepository: WalletRepository,
    private val accountRepository: AccountRepository,
    private val apiService: ApiService,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(SigninViewModel::class.java) -> {
                SigninViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(ResetPasswordViewModel::class.java) -> {
                ResetPasswordViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(ExchangeRateViewModel::class.java) -> {
                ExchangeRateViewModel(exchangeRateRepository) as T
            }
            modelClass.isAssignableFrom(CurrencyConverterViewModel::class.java) -> {
                CurrencyConverterViewModel(currencyRepository) as T
            }
            modelClass.isAssignableFrom(WalletsViewModel::class.java) -> {
                WalletsViewModel(walletRepository) as T
            }
            modelClass.isAssignableFrom(AddAccountViewModel::class.java) -> {
                AddAccountViewModel(apiService,accountRepository) as T
            }
            modelClass.isAssignableFrom(ListAccountsViewModel::class.java) -> {
                ListAccountsViewModel(apiService) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    companion object {
        private var factory: ViewModelFactory? = null

        fun getInstance(
            userRepository: UserRepository,
            exchangeRateRepository: ExchangeRateRepository,
            currencyRepository: CurrencyConverterRepository,
            walletRepository: WalletRepository,
            accountRepository: AccountRepository,
            apiService: ApiService,
        ): ViewModelFactory {
            if (factory == null) {
                synchronized(ViewModelFactory::class.java) {
                    if (factory == null) {
                        factory = ViewModelFactory(
                            userRepository,
                            exchangeRateRepository,
                            currencyRepository,
                            walletRepository,
                            accountRepository,
                            apiService

                        )
                    }
                }
            }
            return factory!!
        }
    }
}