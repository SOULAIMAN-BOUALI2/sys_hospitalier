package com.example.systemhospitalier.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.systemhospitalier.Consultation
import com.example.systemhospitalier.Medecin
import com.example.systemhospitalier.databinding.ActivityConsultationBinding
import com.example.systemhospitalier.models.Patient
import com.example.systemhospitalier.network.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ConsultationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConsultationBinding
    private var patients: List<Patient> = emptyList()
    private var medecinId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsultationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        val userId = intent.getLongExtra("USER_ID", -1)

        loadInitialData(userId)

        binding.btnSaveConsultation.setOnClickListener {
            saveConsultation()
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
                        this@ConsultationActivity,
                        android.R.layout.simple_spinner_dropdown_item,
                        patients.map { "${it.nom} ${it.prenom} (${it.numeroDossier})" }
                    )
                    binding.spinnerPatient.adapter = adapter
                }

            } catch (e: Exception) {
                Log.e("CONSULTATION", "Error loading data: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ConsultationActivity, "Erreur de chargement", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveConsultation() {
        val selectedIndex = binding.spinnerPatient.selectedItemPosition
        if (selectedIndex < 0 || medecinId == -1L) {
            Toast.makeText(this, "Données invalides", Toast.LENGTH_SHORT).show()
            return
        }

        val patient = patients[selectedIndex]
        val motif = binding.etMotif.text.toString()
        val diagnostic = binding.etDiagnostic.text.toString()
        val traitement = binding.etTraitement.text.toString()
        val notes = binding.etNotes.text.toString()

        if (motif.isEmpty() || diagnostic.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir le motif et le diagnostic", Toast.LENGTH_SHORT).show()
            return
        }

        val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        lifecycleScope.launch {
            try {
                val consultation = Consultation(
                    patientId = patient.idPatient,
                    medecinId = medecinId,
                    dateConsultation = currentDate,
                    motif = motif,
                    diagnostic = diagnostic,
                    traitement = traitement,
                    notes = notes
                )

                withContext(Dispatchers.IO) {
                    SupabaseClient.client.postgrest
                        .from("consultation")
                        .insert(consultation)
                }

                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ConsultationActivity, "Consultation enregistrée !", Toast.LENGTH_SHORT).show()
                    finish()
                }

            } catch (e: Exception) {
                Log.e("CONSULTATION", "Error saving: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ConsultationActivity, "Erreur lors de l'enregistrement", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
