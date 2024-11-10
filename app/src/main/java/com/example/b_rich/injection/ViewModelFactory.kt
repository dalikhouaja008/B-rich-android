package com.example.b_rich.injection

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.b_rich.data.repositories.UserRepository
import com.example.b_rich.ui.forgetpassword.ForgetpasswordViewModel
import com.example.b_rich.ui.signin.SigninViewModel
import com.example.b_rich.ui.signup.SignupViewModel

class ViewModelFactory(
    private val userRepository: UserRepository,
    private val sharedPreferences: SharedPreferences // Add SharedPreferences parameter
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(SigninViewModel::class.java) -> {
                SigninViewModel(userRepository, sharedPreferences) as T // Pass SharedPreferences
            }
            modelClass.isAssignableFrom(ForgetpasswordViewModel::class.java) -> {
                ForgetpasswordViewModel(userRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    companion object {
        private var factory: ViewModelFactory? = null

        fun getInstance(userRepository: UserRepository, sharedPreferences: SharedPreferences): ViewModelFactory {
            if (factory == null) {
                synchronized(ViewModelFactory::class.java) {
                    if (factory == null) {
                        factory = ViewModelFactory(userRepository, sharedPreferences)
                    }
                }
            }
            return factory!!
        }
    }
}