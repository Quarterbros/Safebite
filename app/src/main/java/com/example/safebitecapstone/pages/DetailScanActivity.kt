package com.example.safebitecapstone.pages

import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.example.safebitecapstone.databinding.ActivityDetailScanBinding
import com.example.safebitecapstone.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class DetailScanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailScanBinding

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

        binding.ingridientDesc.text = stringBuilder
        dateScanned()
    }


    private fun dateScanned(){

        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val date = Date()
        val current = formatter.format(date)

        println("Waktu : $current")
        binding.timestampDesc.text = current
    }

    companion object {
        var EXTRA_INGRIDIENT = "extra_ingridient"
        var EXTRA_IMG = "extra_img"
    }
}