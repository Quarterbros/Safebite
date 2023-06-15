package com.example.safebitecapstone.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.safebitecapstone.database.Detection
import com.example.safebitecapstone.database.DetectionDao
import com.example.safebitecapstone.database.DetectionRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DetectionRepository(application: Application) {

    private val mDetectionDao: DetectionDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = DetectionRoomDatabase.getDatabase(application)
        mDetectionDao = db.detectionDao()
    }
    fun getAllDetection(): LiveData<List<Detection>> = mDetectionDao.getAllDetection()

    fun insert(detection: Detection) {
        executorService.execute { mDetectionDao.insert(detection) }
    }
    fun delete(detection: Detection) {
        executorService.execute { mDetectionDao.delete(detection) }
    }

}