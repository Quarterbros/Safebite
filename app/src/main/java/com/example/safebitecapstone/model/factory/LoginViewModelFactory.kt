package com.example.safebitecapstone.model.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.safebitecapstone.Injection
import com.example.safebitecapstone.SessionPreferences
import com.example.safebitecapstone.model.LoginViewModel

class LoginViewModelFactory private constructor(private val sessionPreferences: SessionPreferences) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(sessionPreferences) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var instance: LoginViewModelFactory? = null
        fun getInstance(sessionPreferences: SessionPreferences): LoginViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: LoginViewModelFactory(
                    sessionPreferences
                )
            }
    }
}