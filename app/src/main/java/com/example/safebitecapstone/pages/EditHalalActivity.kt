package com.example.safebitecapstone.pages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.safebitecapstone.AddAlergenActivity
import com.example.safebitecapstone.databinding.ActivityEditHalalBinding

class EditHalalActivity : AppCompatActivity() {
    private lateinit var binding : ActivityEditHalalBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditHalalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.halalOnlyTitle.setOnClickListener {
//            If user select halal only preferences
            println("Preferensi : ${binding.halalOnlyTitle.text}")
            startActivity(Intent(this@EditHalalActivity, MainActivity::class.java))
            finish()
        }

        binding.neutralTitle.setOnClickListener {
//            If user select neutral preferences
            println("Preferensi : ${binding.neutralTitle.text}")
            startActivity(Intent(this@EditHalalActivity, MainActivity::class.java))
            finish()
        }
    }

}