package com.example.systemhospitalier.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.systemhospitalier.databinding.ActivityPatientDetailsBinding
import com.example.systemhospitalier.models.Patient
import kotlinx.serialization.json.Json

class PatientDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPatientDetailsBinding
    private var patient: Patient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        val patientJson = intent.getStringExtra("PATIENT_JSON")
        patient = if (patientJson != null) {
            Json.decodeFromString<Patient>(patientJson)
        } else {
            null
        }

        displayPatientInfo()
        setupClickListeners()
    }

    private fun displayPatientInfo() {
        patient?.let {
            binding.tvFullName.text = "${it.nom} ${it.prenom}"
            binding.tvNumeroDossier.text = "N° Dossier: ${it.numeroDossier}"
            binding.chipSexe.text = it.sexe
            binding.chipGroupeSanguin.text = it.groupeSanguin
        }
    }

    private fun setupClickListeners() {
        binding.cardConsultations.setOnClickListener {
            val intent = Intent(this, PatientConsultationsActivity::class.java)
            intent.putExtra("PATIENT_ID", patient?.idPatient)
            startActivity(intent)
        }

        binding.cardHospitalisations.setOnClickListener {
            val intent = Intent(this, PatientHospitalisationsActivity::class.java)
            intent.putExtra("PATIENT_ID", patient?.idPatient)
            startActivity(intent)
        }

        binding.cardPrisesEnCharge.setOnClickListener {
            val intent = Intent(this, PatientPriseEnChargeActivity::class.java)
            intent.putExtra("PATIENT_ID", patient?.idPatient)
            startActivity(intent)
        }
    }
}
