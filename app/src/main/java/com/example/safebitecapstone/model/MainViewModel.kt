package com.example.safebitecapstone.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.safebitecapstone.SessionPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel (private val sessionPreferences: SessionPreferences) : ViewModel(){




    //    Buat cek kalo udah login ga usah relog
    fun userIsLogin(): LiveData<String> {
        return sessionPreferences.getToken().asLiveData()
    }

    fun logout() {
        println("Aku Log out")
        viewModelScope.launch(Dispatchers.IO) {
            sessionPreferences.deleteToken()
        }
    }
}