package com.example.systemhospitalier.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.systemhospitalier.EtapePriseEnCharge
import com.example.systemhospitalier.Medecin
import com.example.systemhospitalier.PriseEnCharge
import com.example.systemhospitalier.adapter.EtapeAdapter
import com.example.systemhospitalier.databinding.ActivityPriseEnChargeBinding
import com.example.systemhospitalier.models.Patient
import com.example.systemhospitalier.network.GeminiService
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
    
    // Pour gérer les étapes
    private var currentPriseEnChargeId: Long? = null
    private var generatedEtapes: List<EtapePriseEnCharge> = emptyList()
    private lateinit var etapeAdapter: EtapeAdapter
    private val geminiService = GeminiService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPriseEnChargeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        val userId = intent.getLongExtra("USER_ID", -1)

        setupDropdowns()
        setupRecyclerView()
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
            if (currentPriseEnChargeId != null && generatedEtapes.isNotEmpty()) {
                saveEtapesInDatabase()
            } else {
                Toast.makeText(this, "Veuillez d'abord enregistrer la prise en charge", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupDropdowns() {
        val urgences = arrayOf("Faible", "Moyen", "Élevé", "Critique")
        binding.autoCompleteUrgence.setAdapter(ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, urgences))

        val types = arrayOf("Standard", "Urgent", "Chirurgical", "Suivi")
        binding.autoCompleteType.setAdapter(ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, types))
        binding.autoCompleteType.setText("Standard", false)

        val etats = arrayOf("En cours", "Terminé", "Annulé", "En attente")
        binding.autoCompleteEtat.setAdapter(ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, etats))
        binding.autoCompleteEtat.setText("En cours", false)
    }

    private fun setupRecyclerView() {
        etapeAdapter = EtapeAdapter(emptyList())
        binding.rvEtapes.layoutManager = LinearLayoutManager(this)
        binding.rvEtapes.adapter = etapeAdapter
    }

    private fun loadInitialData(userId: Long) {
        lifecycleScope.launch {
            binding.progressBar.visibility = View.VISIBLE
            try {
                currentMedecin = withContext(Dispatchers.IO) {
                    SupabaseClient.client.postgrest
                        .from("medecin")
                        .select { filter { eq("id_user", userId) } }
                        .decodeSingleOrNull<Medecin>()
                }

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

        return true
    }

    private fun generateAIProtocol() {
        binding.layoutAIResult.visibility = View.VISIBLE
        binding.tvAIRecommendation.text = "Génération du protocole par Gemini..."
        binding.btnApproveSteps.visibility = View.GONE
        
        val tempPriseEnCharge = PriseEnCharge(
            patientId = selectedPatient?.idPatient ?: 0,
            medecinId = currentMedecin?.idMedecin ?: 0,
            symptomes = binding.etSymptomes.text.toString(),
            constantes = binding.etConstantes.text.toString(),
            niveauUrgence = binding.autoCompleteUrgence.text.toString(),
            observation = binding.etObservation.text.toString()
        )

        lifecycleScope.launch {
            binding.progressBar.visibility = View.VISIBLE
            try {
                generatedEtapes = withContext(Dispatchers.IO) {
                    geminiService.generateEtapes(tempPriseEnCharge)
                }
                
                withContext(Dispatchers.Main) {
                    if (generatedEtapes.isNotEmpty()) {
                        binding.tvAIRecommendation.text = "Protocole généré avec succès (${generatedEtapes.size} étapes) :"
                        etapeAdapter.updateData(generatedEtapes)
                        binding.btnApproveSteps.visibility = View.VISIBLE
                    } else {
                        binding.tvAIRecommendation.text = "Échec de la génération. Veuillez réessayer."
                    }
                }
            } catch (e: Exception) {
                Log.e("GEMINI", "Error: ${e.message}")
                binding.tvAIRecommendation.text = "Erreur IA : ${e.message}"
            } finally {
                binding.progressBar.visibility = View.GONE
            }
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
            type = binding.autoCompleteType.text.toString(),
            etat = binding.autoCompleteEtat.text.toString(),
            symptomes = binding.etSymptomes.text.toString(),
            constantes = binding.etConstantes.text.toString(),
            niveauUrgence = binding.autoCompleteUrgence.text.toString(),
            observation = binding.etObservation.text.toString()
        )

        lifecycleScope.launch {
            binding.progressBar.visibility = View.VISIBLE
            try {
                val inserted = withContext(Dispatchers.IO) {
                    SupabaseClient.client.postgrest
                        .from("prise_en_charge")
                        .insert(priseEnCharge) { select() }
                        .decodeSingle<PriseEnCharge>()
                }
                
                withContext(Dispatchers.Main) {
                    currentPriseEnChargeId = inserted.idPriseEnCharge
                    Toast.makeText(this@PriseEnChargeActivity, "Prise en charge enregistrée ! ID: $currentPriseEnChargeId", Toast.LENGTH_SHORT).show()
                    
                    // On met à jour l'ID dans les étapes générées si elles existent
                    generatedEtapes = generatedEtapes.map { it.copy(priseEnChargeId = currentPriseEnChargeId!!) }
                    
                    // On ne fait PAS finish() pour rester sur la page
                    binding.btnSave.isEnabled = false // Empêcher les doublons
                    binding.btnSave.text = "Enregistré"
                }
            } catch (e: Exception) {
                Log.e("PRISE_EN_CHARGE", "Error: ${e.message}")
                Toast.makeText(this@PriseEnChargeActivity, "Erreur d'enregistrement", Toast.LENGTH_SHORT).show()
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun saveEtapesInDatabase() {
        lifecycleScope.launch {
            binding.progressBar.visibility = View.VISIBLE
            try {
                withContext(Dispatchers.IO) {
                    SupabaseClient.client.postgrest
                        .from("etape_prise_en_charge")
                        .insert(generatedEtapes)
                }
                
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@PriseEnChargeActivity, "Toutes les étapes ont été enregistrées !", Toast.LENGTH_LONG).show()
                    finish() // On peut maintenant quitter
                }
            } catch (e: Exception) {
                Log.e("ETAPES", "Error saving steps: ${e.message}")
                Toast.makeText(this@PriseEnChargeActivity, "Erreur lors de l'enregistrement des étapes", Toast.LENGTH_SHORT).show()
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }
}
