package com.example.b_rich.injection

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.b_rich.data.repositories.ExchangeRateRepository
import com.example.b_rich.data.repositories.UserRepository
import com.example.b_rich.ui.exchange_rate.ExchangeRateViewModel
import com.example.b_rich.ui.forgetpassword.ForgetpasswordViewModel
import com.example.b_rich.ui.signin.SigninViewModel
import com.example.b_rich.ui.signup.SignupViewModel


class ViewModelFactory(private val userRepository: UserRepository,private val exchangeRateRepository: ExchangeRateRepository) : ViewModelProvider.Factory {
class ViewModelFactory(
    private val userRepository: UserRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(SigninViewModel::class.java) -> {
                SigninViewModel(userRepository, sharedPreferences) as T
            }
            modelClass.isAssignableFrom(ForgetpasswordViewModel::class.java) -> {
                ForgetpasswordViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(ExchangeRateViewModel::class.java) -> {
                ForgetpasswordViewModel(userRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    companion object {
        private var factory: ViewModelFactory? = null

        fun getInstance(userRepository: UserRepository, sharedPreferences: SharedPreferences,exchangeRateRepository: ExchangeRateRepository): ViewModelFactory {
            if (factory == null) {
                synchronized(ViewModelFactory::class.java) {
                    if (factory == null) {
                        factory = ViewModelFactory(userRepository, sharedPreferences ,exchangeRateRepository)
                    }
                }
            }
            return factory!!
        }
    }
}