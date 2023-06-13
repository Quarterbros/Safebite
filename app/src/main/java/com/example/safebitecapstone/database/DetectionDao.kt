package com.example.safebitecapstone.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DetectionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(detection: Detection)

    @Delete
    fun delete(detection: Detection)

    @Query("SELECT * from detection ORDER BY timestamp DESC")
    fun getAllDetection(): LiveData<List<Detection>>
}