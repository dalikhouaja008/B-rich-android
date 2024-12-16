package com.example.b_rich.injection

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.b_rich.data.repositories.CurrencyConverterRepository
import com.example.b_rich.data.repositories.ExchangeRateRepository
import com.example.b_rich.data.repositories.UserRepository
import com.example.b_rich.data.repositories.WalletRepository
import com.example.b_rich.ui.AddAccount.AddAccountViewModel
import com.example.b_rich.ui.currency_converter.CurrencyConverterViewModel
import com.example.b_rich.ui.exchange_rate.ExchangeRateViewModel
import com.example.b_rich.ui.forgetpassword.ForgetpasswordViewModel
import com.example.b_rich.ui.resetPassword.ResetPasswordViewModel
import com.example.b_rich.ui.signin.SigninViewModel
import com.example.b_rich.ui.signup.SignupViewModel
import com.example.b_rich.ui.wallets.WalletsViewModel

class ViewModelFactory(
    private val userRepository: UserRepository,
    private val exchangeRateRepository: ExchangeRateRepository,
    private val currencyRepository: CurrencyConverterRepository,
    private val walletRepository: WalletRepository,
    private val context: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
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
            modelClass.isAssignableFrom(ForgetpasswordViewModel::class.java) -> {
                ForgetpasswordViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(ExchangeRateViewModel::class.java) -> {
                ExchangeRateViewModel(exchangeRateRepository) as T
            }
            modelClass.isAssignableFrom(AddAccountViewModel::class.java) -> {
                AddAccountViewModel() as T
            }
            modelClass.isAssignableFrom(CurrencyConverterViewModel::class.java) -> {
                CurrencyConverterViewModel(currencyRepository) as T
            }
            modelClass.isAssignableFrom(WalletsViewModel::class.java) -> {
                WalletsViewModel(walletRepository, context) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(
            userRepository: UserRepository,
            exchangeRateRepository: ExchangeRateRepository,
            currencyRepository: CurrencyConverterRepository,
            walletRepository: WalletRepository,
            context: Context
        ): ViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ViewModelFactory(
                    userRepository,
                    exchangeRateRepository,
                    currencyRepository,
                    walletRepository,
                    context
                ).also { INSTANCE = it }
            }
        }
    }
}