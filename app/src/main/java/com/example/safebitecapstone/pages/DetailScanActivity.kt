package com.example.safebitecapstone.pages

import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.example.safebitecapstone.R
import com.example.safebitecapstone.SessionPreferences
import com.example.safebitecapstone.database.Detection
import com.example.safebitecapstone.databinding.ActivityDetailScanBinding
import com.example.safebitecapstone.databinding.ActivityMainBinding
import com.example.safebitecapstone.model.DetectionDataLocalViewModel
import com.example.safebitecapstone.model.LoginViewModel
import com.example.safebitecapstone.model.factory.HistoryViewModelFactory
import com.example.safebitecapstone.model.factory.LoginViewModelFactory
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class DetailScanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailScanBinding

    private val detectionDataLocalViewModel: DetectionDataLocalViewModel by viewModels {
        HistoryViewModelFactory.getInstance(application)
    }

    private var detection: Detection? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Detail Product"


        val img : Uri? = intent.getParcelableExtra(EXTRA_IMG)
        binding.photoDetail.setImageURI(img)

        val ingridientData = intent.getStringExtra(EXTRA_INGRIDIENT)
        println("Nilai ingridientData " + ingridientData.toString())

        val stringBuilder = ingridientData?.replace("\\s".toRegex(), ", ")

        println("Ingridients : $stringBuilder")


        detection = intent.getParcelableExtra(EXTRA_DATA)
        println("Cek is null $detection")
        if (detection == null){
            detection = Detection()
            binding.ingridientDesc.text = stringBuilder
            dateScanned()

            detection.let {
                detection?.timestamp = binding.timestampDesc.text.toString()
                detection?.hallal = binding.hallalDesc.text.toString()
                detection?.potentialAllergies = binding.allergiesDesc.text.toString()
                detection?.potentialDisease = binding.diseaseDesc.text.toString()
                detection?.ingridient = binding.ingridientDesc.text.toString()
            }
            if (detection != null){
                detectionDataLocalViewModel.insert(detection as Detection)
            }
        }
        else {
            detection.let {
                binding.photoDetail.setImageResource(R.drawable.baseline_fastfood_24)
                binding?.hallalDesc?.setText(detection!!.hallal)
                binding?.allergiesDesc?.setText(detection!!.potentialAllergies)
                binding?.diseaseDesc?.setText(detection!!.potentialDisease)
                binding?.ingridientDesc?.setText(detection!!.ingridient)
                binding?.timestampDesc?.setText(detection!!.timestamp)
            }
        }




    }

    private fun dateScanned(){

        val formatter = SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss")
        val date = Date()
        val current = formatter.format(date)

        binding.timestampDesc.text = current
    }


    companion object {
        var EXTRA_INGRIDIENT = "extra_ingridient"
        var EXTRA_IMG = "extra_img"
        var EXTRA_DATA = "extra_data"
    }
}