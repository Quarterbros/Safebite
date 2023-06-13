package com.example.safebitecapstone.model

import android.content.ContentValues.TAG
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.safebitecapstone.API.ApiConfig
import com.example.safebitecapstone.API.DetectionResponse
import com.example.safebitecapstone.SessionPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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