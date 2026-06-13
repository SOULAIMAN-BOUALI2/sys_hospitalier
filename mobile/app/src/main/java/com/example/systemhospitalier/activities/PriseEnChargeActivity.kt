package com.example.systemhospitalier.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.systemhospitalier.Medecin
import com.example.systemhospitalier.PriseEnCharge
import com.example.systemhospitalier.databinding.ActivityPriseEnChargeBinding
import com.example.systemhospitalier.models.Patient
import com.example.systemhospitalier.network.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PriseEnChargeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPriseEnChargeBinding
    private var patients: List<Patient> = emptyList()
    private var selectedPatient: Patient? = null
    private var currentMedecin: Medecin? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPriseEnChargeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        val userId = intent.getLongExtra("USER_ID", -1)

        setupUrgenceDropdown()
        loadInitialData(userId)

        binding.btnGenerateIA.setOnClickListener {
            if (validateForm()) {
                generateAIProtocol()
            }
        }

        binding.btnSave.setOnClickListener {
            if (validateForm()) {
                savePriseEnCharge()
            }
        }

        binding.btnApproveSteps.setOnClickListener {
            Toast.makeText(this, "Étapes approuvées et prêtes à être enregistrées", Toast.LENGTH_SHORT).show()
            // Logique future pour enregistrer les EtapePriseEnCharge
        }
    }

    private fun setupUrgenceDropdown() {
        val urgences = arrayOf("Faible", "Moyen", "Élevé", "Critique")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, urgences)
        binding.autoCompleteUrgence.setAdapter(adapter)
    }

    private fun loadInitialData(userId: Long) {
        lifecycleScope.launch {
            binding.progressBar.visibility = View.VISIBLE
            try {
                // Fetch Medecin
                currentMedecin = withContext(Dispatchers.IO) {
                    SupabaseClient.client.postgrest
                        .from("medecin")
                        .select {
                            filter { eq("id_user", userId) }
                        }
                        .decodeSingleOrNull<Medecin>()
                }

                // Fetch Patients
                patients = withContext(Dispatchers.IO) {
                    SupabaseClient.client.postgrest
                        .from("patient")
                        .select()
                        .decodeList<Patient>()
                }

                withContext(Dispatchers.Main) {
                    val patientNames = patients.map { "${it.nom} ${it.prenom} (${it.numeroDossier})" }
                    val adapter = ArrayAdapter(this@PriseEnChargeActivity, android.R.layout.simple_dropdown_item_1line, patientNames)
                    binding.autoCompletePatient.setAdapter(adapter)
                    
                    binding.autoCompletePatient.setOnItemClickListener { _, _, position, _ ->
                        selectedPatient = patients[position]
                    }
                }
            } catch (e: Exception) {
                Log.e("PRISE_EN_CHARGE", "Error loading data: ${e.message}")
                Toast.makeText(this@PriseEnChargeActivity, "Erreur de chargement", Toast.LENGTH_SHORT).show()
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun validateForm(): Boolean {
        if (selectedPatient == null) {
            binding.tilPatient.error = "Veuillez sélectionner un patient"
            return false
        } else binding.tilPatient.error = null

        if (binding.etSymptomes.text.isNullOrBlank()) {
            binding.tilSymptomes.error = "Champ requis"
            return false
        } else binding.tilSymptomes.error = null

        if (binding.autoCompleteUrgence.text.isNullOrBlank()) {
            binding.tilUrgence.error = "Champ requis"
            return false
        } else binding.tilUrgence.error = null

        return true
    }

    private fun generateAIProtocol() {
        binding.layoutAIResult.visibility = View.VISIBLE
        binding.tvAIRecommendation.text = "Génération du protocole par l'IA en cours..."
        
        lifecycleScope.launch {
            // Simulation d'appel LLM (Gemini/Ollama)
            withContext(Dispatchers.IO) {
                kotlinx.coroutines.delay(2000)
            }
            
            val prompt = """
                En tant qu'assistant médical IA, voici un protocole suggéré pour :
                Patient: ${selectedPatient?.nom} ${selectedPatient?.prenom}
                Symptômes: ${binding.etSymptomes.text}
                Constantes: ${binding.etConstantes.text}
                Urgence: ${binding.autoCompleteUrgence.text}
                
                Recommandations :
                1. Surveillance continue des constantes.
                2. Administration de soins de support immédiats.
                3. Examens complémentaires (Bilan sanguin, ECG).
                4. Consultation spécialisée si les symptômes persistent.
            """.trimIndent()
            
            binding.tvAIRecommendation.text = prompt
        }
    }

    private fun savePriseEnCharge() {
        val medecin = currentMedecin ?: return
        val patient = selectedPatient ?: return

        val currentDate = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())

        val priseEnCharge = PriseEnCharge(
            patientId = patient.idPatient,
            medecinId = medecin.idMedecin,
            dateDebut = currentDate,
            symptomes = binding.etSymptomes.text.toString(),
            constantes = binding.etConstantes.text.toString(),
            niveauUrgence = binding.autoCompleteUrgence.text.toString(),
            observation = binding.etObservation.text.toString(),
            recommandationsIA = binding.tvAIRecommendation.text.toString()
        )

        lifecycleScope.launch {
            binding.progressBar.visibility = View.VISIBLE
            try {
                withContext(Dispatchers.IO) {
                    SupabaseClient.client.postgrest
                        .from("prise_en_charge")
                        .insert(priseEnCharge)
                }
                
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@PriseEnChargeActivity, "Prise en charge enregistrée !", Toast.LENGTH_LONG).show()
                    finish()
                }
            } catch (e: Exception) {
                Log.e("PRISE_EN_CHARGE", "Error saving: ${e.message}")
                Toast.makeText(this@PriseEnChargeActivity, "Erreur lors de l'enregistrement", Toast.LENGTH_SHORT).show()
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }
}
