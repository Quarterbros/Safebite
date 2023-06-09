package com.example.safebitecapstone.model

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.safebitecapstone.database.Detection
import com.example.safebitecapstone.repository.DetectionRepository


class DetectionDataLocalViewModel(application: Application) : ViewModel() {

    private val mDetectionRepository: DetectionRepository = DetectionRepository(application)

    fun insert(detection: Detection) {
        mDetectionRepository.insert(detection)
    }

    fun delete(detection: Detection) {
        mDetectionRepository.delete(detection)
    }
}
