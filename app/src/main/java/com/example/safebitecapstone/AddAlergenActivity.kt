package com.example.safebitecapstone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.safebitecapstone.databinding.ActivityAddAlergenBinding
import com.example.safebitecapstone.databinding.ActivityDetailScanBinding
import com.example.safebitecapstone.databinding.ActivityMainBinding

class AddAlergenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddAlergenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAlergenBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}