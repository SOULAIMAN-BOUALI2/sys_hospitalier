package com.example.systemhospitalier.activities

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.systemhospitalier.Hospitalisation
import com.example.systemhospitalier.databinding.ActivityGenericDetailBinding
import kotlinx.serialization.json.Json

class HospitalisationDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGenericDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenericDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }
        supportActionBar?.title = "Détails Hospitalisation"

        val jsonString = intent.getStringExtra("HOSPITALISATION_JSON")
        if (jsonString != null) {
            val item = Json.decodeFromString<Hospitalisation>(jsonString)
            displayDetails(item)
        }
    }

    private fun displayDetails(item: Hospitalisation) {
        addDetailRow("Date d'admission", item.dateAdmission)
        addDetailRow("Date de sortie", item.dateSortie ?: "Toujours hospitalisé")
        addDetailRow("Chambre", item.chambre)
        addDetailRow("Motif", item.motif)
        addDetailRow("État du patient", item.etatPatient)
        addDetailRow("Diagnostic initial", item.diagnosticInitial)
    }

    private fun addDetailRow(label: String, value: String) {
        val textView = TextView(this)
        textView.text = "$label :\n$value\n"
        textView.textSize = 16f
        textView.setPadding(0, 10, 0, 10)
        binding.detailsContainer.addView(textView)
    }
}
