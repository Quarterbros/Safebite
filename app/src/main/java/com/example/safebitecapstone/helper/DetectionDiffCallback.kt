package com.example.safebitecapstone.helper

import androidx.recyclerview.widget.DiffUtil
import com.example.safebitecapstone.database.Detection

class DetectionDiffCallback(private val oldDetectionList: List<Detection>, private val newDetectionList: List<Detection>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldDetectionList.size
    override fun getNewListSize(): Int = newDetectionList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldDetectionList[oldItemPosition].id == newDetectionList[newItemPosition].id
    }
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldData = oldDetectionList[oldItemPosition]
        val newData = newDetectionList[newItemPosition]
        return oldData.id == newData.id
    }
}