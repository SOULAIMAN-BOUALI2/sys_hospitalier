package com.example.systemhospitalier.activities

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.systemhospitalier.Hospitalisation
import com.example.systemhospitalier.Medecin
import com.example.systemhospitalier.databinding.ActivityHospitalisationBinding
import com.example.systemhospitalier.models.Patient
import com.example.systemhospitalier.network.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HospitalisationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHospitalisationBinding
    private var patients: List<Patient> = emptyList()
    private var medecinId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHospitalisationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        val userId = intent.getLongExtra("USER_ID", -1)

        loadInitialData(userId)

        binding.btnSaveHospitalisation.setOnClickListener {
            saveHospitalisation()
        }
    }

    private fun loadInitialData(userId: Long) {
        lifecycleScope.launch {
            try {
                // 1. Get Medecin ID
                val medecin = withContext(Dispatchers.IO) {
                    SupabaseClient.client.postgrest
                        .from("medecin")
                        .select { filter { eq("id_user", userId) } }
                        .decodeSingleOrNull<Medecin>()
                }
                medecinId = medecin?.idMedecin ?: -1

                // 2. Get Patients for spinner
                patients = withContext(Dispatchers.IO) {
                    SupabaseClient.client.postgrest
                        .from("patient")
                        .select()
                        .decodeList<Patient>()
                }

                withContext(Dispatchers.Main) {
                    val adapter = ArrayAdapter(
                        this@HospitalisationActivity,
                        android.R.layout.simple_spinner_dropdown_item,
                        patients.map { "${it.nom} ${it.prenom} (${it.numeroDossier})" }
                    )
                    binding.spinnerPatient.adapter = adapter
                }

            } catch (e: Exception) {
                Log.e("HOSPITALISATION", "Error loading data: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@HospitalisationActivity, "Erreur de chargement", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveHospitalisation() {
        val selectedIndex = binding.spinnerPatient.selectedItemPosition
        if (selectedIndex < 0 || medecinId == -1L) {
            Toast.makeText(this, "Données invalides", Toast.LENGTH_SHORT).show()
            return
        }

        val patient = patients[selectedIndex]
        val chambre = binding.etChambre.text.toString().trim()
        val etat = binding.etEtat.text.toString().trim()
        val motif = binding.etMotif.text.toString().trim()
        val diagnostic = binding.etDiagnosticInitial.text.toString().trim()

        if (chambre.isEmpty() || motif.isEmpty() || diagnostic.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs obligatoires", Toast.LENGTH_SHORT).show()
            return
        }

        val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        lifecycleScope.launch {
            try {
                val hospitalisation = Hospitalisation(
                    dateAdmission = currentDate,
                    chambre = chambre,
                    motif = motif,
                    etatPatient = etat,
                    diagnosticInitial = diagnostic,
                    idPatient = patient.idPatient,
                    idMedecin = medecinId
                )

                withContext(Dispatchers.IO) {
                    SupabaseClient.client.postgrest
                        .from("hospitalisation")
                        .insert(hospitalisation)
                }

                withContext(Dispatchers.Main) {
                    Toast.makeText(this@HospitalisationActivity, "Hospitalisation enregistrée !", Toast.LENGTH_SHORT).show()
                    finish()
                }

            } catch (e: Exception) {
                Log.e("HOSPITALISATION", "Error saving: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@HospitalisationActivity, "Erreur lors de l'enregistrement", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
