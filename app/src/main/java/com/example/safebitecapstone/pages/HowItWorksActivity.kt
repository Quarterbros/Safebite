package com.example.safebitecapstone.pages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.safebitecapstone.databinding.ActivityAboutBinding
import com.example.safebitecapstone.databinding.ActivityHowItWorksBinding

class HowItWorksActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHowItWorksBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHowItWorksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "How It Works ?"
    }
}