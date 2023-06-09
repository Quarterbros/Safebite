package com.example.safebitecapstone.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "Detection")
@Parcelize
class Detection (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "timestamp")
    var timestamp: String? = null,

    @ColumnInfo(name = "isHallal")
    var hallal: String? = null,

    @ColumnInfo(name = "potential_allergies")
    var potentialAllergies: String? = null,

    @ColumnInfo(name = "potential_disease")
    var potentialDisease: String? = null,

    @ColumnInfo(name = "ingridient")
    var ingridient: String? = null

) : Parcelable