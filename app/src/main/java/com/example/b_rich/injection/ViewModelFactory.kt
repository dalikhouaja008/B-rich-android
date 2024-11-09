package com.example.b_rich.injection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.b_rich.data.repositories.UserRepository
import com.example.b_rich.ui.forgetpassword.ForgetpasswordViewModel
import com.example.b_rich.ui.signin.SigninViewModel
import com.example.b_rich.ui.signup.SignupViewModel


class ViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(SigninViewModel::class.java) -> {
                SigninViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(ForgetpasswordViewModel::class.java) -> {
                ForgetpasswordViewModel(userRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    companion object {
        private var factory: ViewModelFactory? = null

        fun getInstance(userRepository: UserRepository): ViewModelFactory {
            if (factory == null) {
                synchronized(ViewModelFactory::class.java) {
                    if (factory == null) {
                        factory = ViewModelFactory(userRepository)
                    }
                }
            }
            return factory!!
        }
    }
}