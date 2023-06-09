package com.example.safebitecapstone.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Detection::class], version = 1)
abstract class DetectionRoomDatabase : RoomDatabase() {

    abstract fun detectionDao(): DetectionDao
    companion object {
        @Volatile
        private var INSTANCE: DetectionRoomDatabase? = null
        @JvmStatic
        fun getDatabase(context: Context): DetectionRoomDatabase {
            if (INSTANCE == null) {
                synchronized(DetectionRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        DetectionRoomDatabase::class.java, "detection_database")
                        .build()
                }
            }
            return INSTANCE as DetectionRoomDatabase
        }
    }
}