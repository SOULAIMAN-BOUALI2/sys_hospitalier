package com.example.systemhospitalier.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.systemhospitalier.R
import com.example.systemhospitalier.models.Patient
import com.example.systemhospitalier.network.SupabaseClient
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AIAnalysisActivity : AppCompatActivity() {

    private lateinit var layoutLoading: LinearLayout
    private lateinit var layoutResult: LinearLayout
    private lateinit var tvAIResponse: TextView
    private lateinit var tvPatientName: TextView
    private lateinit var btnValidateProtocol: MaterialButton
    private lateinit var spinnerPatientIA: Spinner
    private lateinit var btnGenerateAnalysis: Button
    
    private var patients: List<Patient> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ai_analysis)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        layoutLoading = findViewById(R.id.layoutLoading)
        layoutResult = findViewById(R.id.layoutResult)
        tvAIResponse = findViewById(R.id.tvAIResponse)
        tvPatientName = findViewById(R.id.tvPatientName)
        btnValidateProtocol = findViewById(R.id.btnValidateProtocol)
        spinnerPatientIA = findViewById(R.id.spinnerPatientIA)
        btnGenerateAnalysis = findViewById(R.id.btnGenerateAnalysis)

        loadPatients()

        btnGenerateAnalysis.setOnClickListener {
            val selectedIndex = spinnerPatientIA.selectedItemPosition
            if (selectedIndex >= 0) {
                val patient = patients[selectedIndex]
                tvPatientName.text = "Patient: ${patient.nom} ${patient.prenom}"
                simulateAIAnalysis(patient)
            } else {
                Toast.makeText(this, "Veuillez sélectionner un patient", Toast.LENGTH_SHORT).show()
            }
        }

        btnValidateProtocol.setOnClickListener {
            finish()
        }
    }

    private fun loadPatients() {
        lifecycleScope.launch {
            try {
                patients = withContext(Dispatchers.IO) {
                    SupabaseClient.client.postgrest
                        .from("patient")
                        .select()
                        .decodeList<Patient>()
                }

                withContext(Dispatchers.Main) {
                    val adapter = ArrayAdapter(
                        this@AIAnalysisActivity,
                        android.R.layout.simple_spinner_dropdown_item,
                        patients.map { "${it.nom} ${it.prenom} (${it.numeroDossier})" }
                    )
                    spinnerPatientIA.adapter = adapter
                }
            } catch (e: Exception) {
                Log.e("AI_ANALYSIS", "Error loading patients: ${e.message}")
            }
        }
    }

    private fun simulateAIAnalysis(patient: Patient) {
        layoutLoading.visibility = View.VISIBLE
        layoutResult.visibility = View.GONE

        // Simulation d'un délai réseau
        tvAIResponse.postDelayed({
            layoutLoading.visibility = View.GONE
            layoutResult.visibility = View.VISIBLE
            
            val analysis = """
                Analyse IA pour ${patient.nom} ${patient.prenom} :
                
                1. Vérification du groupe sanguin (${patient.groupeSanguin ?: "N/A"}).
                2. Examen clinique général requis.
                3. Mise en place d'un protocole de surveillance standard.
                4. Prise des constantes (T°, Tension, Pouls).
                5. Établir un plan de soin personnalisé après consultation.
            """.trimIndent()
            
            tvAIResponse.text = analysis
        }, 2000)
    }
}
