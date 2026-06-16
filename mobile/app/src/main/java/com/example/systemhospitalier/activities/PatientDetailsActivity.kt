package com.example.systemhospitalier.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.systemhospitalier.databinding.ActivityPatientDetailsBinding
import com.example.systemhospitalier.models.Patient
import com.example.systemhospitalier.network.SupabaseClient
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

        binding.btnDeletePatient.setOnClickListener {
            showDeleteConfirmation()
        }
    }

    private fun showDeleteConfirmation() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Confirmation de suppression")
            .setMessage("Êtes-vous sûr de vouloir supprimer ce patient ?\nCette action supprimera également toutes les consultations, hospitalisations, prises en charge et étapes de prise en charge associées.")
            .setNegativeButton("Annuler", null)
            .setPositiveButton("Supprimer") { _, _ ->
                deletePatient()
            }
            .show()
    }

    private fun deletePatient() {
        val patientId = patient?.idPatient ?: return

        lifecycleScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    // 1. Récupérer les IDs des prises en charge pour supprimer les étapes
                    val pecIds = SupabaseClient.client.postgrest
                        .from("prise_en_charge")
                        .select {
                            filter { eq("patient_id", patientId) }
                        }
                        .decodeList<PriseEnChargeIdOnly>()
                        .map { it.idPriseEnCharge }

                    if (pecIds.isNotEmpty()) {
                        // 2. Supprimer les étapes de prise en charge
                        SupabaseClient.client.postgrest
                            .from("etape_prise_en_charge")
                            .delete {
                                filter {
                                    isIn("prise_en_charge_id", pecIds)
                                }
                            }
                    }

                    // 3. Supprimer les prises en charge
                    SupabaseClient.client.postgrest
                        .from("prise_en_charge")
                        .delete {
                            filter { eq("patient_id", patientId) }
                        }

                    // 4. Supprimer les consultations
                    SupabaseClient.client.postgrest
                        .from("consultation")
                        .delete {
                            filter { eq("patient_id", patientId) }
                        }

                    // 5. Supprimer les hospitalisations
                    SupabaseClient.client.postgrest
                        .from("hospitalisation")
                        .delete {
                            filter { eq("id_patient", patientId) }
                        }

                    // 6. Supprimer le patient
                    SupabaseClient.client.postgrest
                        .from("patient")
                        .delete {
                            filter { eq("id_patient", patientId) }
                        }
                }

                Toast.makeText(this@PatientDetailsActivity, "Patient supprimé avec succès", Toast.LENGTH_SHORT).show()
                finish()

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@PatientDetailsActivity, "Erreur lors de la suppression: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    @kotlinx.serialization.Serializable
    private data class PriseEnChargeIdOnly(
        @kotlinx.serialization.SerialName("id_prise_en_charge") val idPriseEnCharge: Long
    )
}
