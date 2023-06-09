package com.example.safebitecapstone.model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.safebitecapstone.database.Detection
import com.example.safebitecapstone.repository.DetectionRepository


class HistoryViewModel(application: Application) : ViewModel() {

    private val mDetectionRepository: DetectionRepository = DetectionRepository(application)

    fun getAllDetection(): LiveData<List<Detection>> = mDetectionRepository.getAllDetection()
}
