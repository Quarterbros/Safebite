package com.example.safebitecapstone.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safebitecapstone.SessionPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel (private val sessionPreferences: SessionPreferences) : ViewModel() {

    fun userToken(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            sessionPreferences.saveToken(token)
        }
    }
}