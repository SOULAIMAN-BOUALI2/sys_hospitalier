package com.example.systemhospitalier.activities

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.systemhospitalier.Consultation
import com.example.systemhospitalier.databinding.ActivityGenericDetailBinding
import kotlinx.serialization.json.Json

class ConsultationDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGenericDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenericDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }
        supportActionBar?.title = "Détails Consultation"

        val jsonString = intent.getStringExtra("CONSULTATION_JSON")
        if (jsonString != null) {
            val item = Json.decodeFromString<Consultation>(jsonString)
            displayDetails(item)
        }
    }

    private fun displayDetails(item: Consultation) {
        addDetailRow("Date", item.dateConsultation)
        addDetailRow("Motif", item.motif)
        addDetailRow("Diagnostic", item.diagnostic)
        addDetailRow("Traitement", item.traitement)
        addDetailRow("Notes", item.notes ?: "Aucune note")
    }

    private fun addDetailRow(label: String, value: String) {
        val textView = TextView(this)
        textView.text = "$label :\n$value\n"
        textView.textSize = 16f
        textView.setPadding(0, 10, 0, 10)
        binding.detailsContainer.addView(textView)
    }
}
