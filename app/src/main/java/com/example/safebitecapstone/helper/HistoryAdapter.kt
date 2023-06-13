package com.example.safebitecapstone.helper

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.safebitecapstone.database.Detection
import com.example.safebitecapstone.databinding.AlergenItemBinding
import com.example.safebitecapstone.pages.DetailScanActivity
import kotlin.coroutines.coroutineContext

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private val listDetection = ArrayList<Detection>()

    fun setListDetection(listNotes: List<Detection>) {
        println("Masuk setListDetection")
        val diffCallback = DetectionDiffCallback(this.listDetection, listNotes)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listDetection.clear()
        this.listDetection.addAll(listNotes)
        diffResult.dispatchUpdatesTo(this)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = AlergenItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }
    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(listDetection[position])
    }
    override fun getItemCount(): Int {
        println("Ukuran data ${listDetection.size}")
        return listDetection.size
    }
    inner class HistoryViewHolder(private val binding: AlergenItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(detection: Detection) {
            with(binding) {

//                tvTitle.text = "Scanned In ${detection.timestamp}"
                tvTitle.text = "${detection.name}"

                cardView.setOnClickListener {
                    val intent = Intent(it.context, DetailScanActivity::class.java)
                    intent.putExtra(DetailScanActivity.EXTRA_DATA, detection)
                    it.context.startActivity(intent)
                }
            }
        }
    }
}