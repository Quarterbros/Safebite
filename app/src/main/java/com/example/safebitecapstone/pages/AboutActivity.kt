package com.example.safebitecapstone.pages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.safebitecapstone.databinding.ActivityAboutBinding
import com.example.safebitecapstone.databinding.ActivityDetailScanBinding

class AboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "About Us"
    }
}