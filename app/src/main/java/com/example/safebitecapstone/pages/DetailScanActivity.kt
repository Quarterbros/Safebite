package com.example.safebitecapstone.pages

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.safebitecapstone.R
import com.example.safebitecapstone.database.Detection
import com.example.safebitecapstone.databinding.ActivityDetailScanBinding
import com.example.safebitecapstone.model.DetectionDataLocalViewModel
import com.example.safebitecapstone.model.factory.HistoryViewModelFactory

import java.text.SimpleDateFormat
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

        val img : Uri? = intent.getParcelableExtra(EXTRA_IMG)
        binding.photoDetail.setImageURI(img)

        val ingridientData = intent.getStringExtra(EXTRA_INGRIDIENT)

        val stringBuilder = ingridientData?.replace(Regex("ingredients", RegexOption.IGNORE_CASE), "")


        val dataHalal = intent.getStringExtra(EXTRA_HALAL)
        val dataDisease = intent.getStringExtra(EXTRA_DISEASE)
        val dataAllergy = intent.getStringExtra(EXTRA_ALLERGY)
        val dataTitle = intent.getStringExtra(EXTRA_TITLE)

        detection = intent.getParcelableExtra(EXTRA_DATA)

        if (detection == null){
            detection = Detection()
            binding.ingridientDesc.text = stringBuilder.toString()
            supportActionBar?.title = "Detail $dataTitle"

            if (dataHalal != null && dataDisease != null && dataAllergy != null){
                setResultData(dataHalal, dataAllergy, dataDisease)
            }
            dateScanned()

            detection.let {
                detection?.name = dataTitle
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

//        Ini buat ngeset value di halaman history
        else {
            detection.let {
                supportActionBar?.title = "Detail ${detection!!.name}"
                binding.photoDetail.setImageResource(R.drawable.baseline_fastfood_24)
                binding.hallalDesc.text = detection!!.hallal
                binding.allergiesDesc.text = detection!!.potentialAllergies
                binding.diseaseDesc.text = detection!!.potentialDisease
                binding.ingridientDesc.text = detection!!.ingridient
                binding.timestampDesc.setText(detection!!.timestamp)
            }
        }

    }

    private fun dateScanned(){

        val formatter = SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss")
        val date = Date()
        val current = formatter.format(date)

        binding.timestampDesc.text = current
    }

    private fun setResultData(valueHalal: String, valueAllergy: String, valueDisease: String) {
        if (valueHalal == "Halal"){
            binding.hallalDesc.text = "Yes, it's Hallal"
        }
        else if (valueHalal == "Haram"){
            binding.hallalDesc.text = "No, it's Haram"
        }
        else{
            binding.hallalDesc.text = "Sorry, we can't detect it"
        }
        binding.allergiesDesc.text = valueAllergy
        binding.diseaseDesc.text = valueDisease
    }

    companion object {
        var EXTRA_INGRIDIENT = "extra_ingridient"
        var EXTRA_IMG = "extra_img"
        var EXTRA_DATA = "extra_data"

        var EXTRA_HALAL = "extra_halal"
        var EXTRA_ALLERGY = "extra_allergy"
        var EXTRA_DISEASE = "extra_disease"
        var EXTRA_TITLE = "extra_title"
    }
}