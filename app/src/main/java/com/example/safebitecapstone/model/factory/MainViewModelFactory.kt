package com.example.safebitecapstone.model.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.safebitecapstone.SessionPreferences
import com.example.safebitecapstone.model.MainViewModel

class MainViewModelFactory private constructor(private val sessionPreferences: SessionPreferences) :
    ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(sessionPreferences) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var instance: MainViewModelFactory? = null

        fun getInstance(
            context: Context,
            sessionPreferences: SessionPreferences
        ): MainViewModelFactory = instance ?: synchronized(this) {
            instance ?: MainViewModelFactory(
                sessionPreferences
            )
        }.also { instance = it }
    }
}