package com.example.safebitecapstone.pages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.safebitecapstone.databinding.ActivityEditHalalBinding

class EditHalalActivity : AppCompatActivity() {
    private lateinit var binding : ActivityEditHalalBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditHalalBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}