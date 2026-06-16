package com.example.systemhospitalier.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.systemhospitalier.databinding.ActivityPatientDetectionBinding

class PatientDetectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPatientDetectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientDetectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.btnCamera.setOnClickListener {
            Toast.makeText(this, "Fonctionnalité de capture caméra en développement", Toast.LENGTH_SHORT).show()
        }

        binding.btnGallery.setOnClickListener {
            Toast.makeText(this, "Sélection depuis la galerie en développement", Toast.LENGTH_SHORT).show()
        }
    }
}