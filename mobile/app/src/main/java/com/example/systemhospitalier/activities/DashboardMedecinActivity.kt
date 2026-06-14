package com.example.systemhospitalier.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.systemhospitalier.Medecin
import com.example.systemhospitalier.Utilisateur
import com.example.systemhospitalier.adapter.PatientAdapter
import com.example.systemhospitalier.databinding.ActivityDashboardMedecinBinding
import com.example.systemhospitalier.models.Patient
import com.example.systemhospitalier.network.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardMedecinActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardMedecinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardMedecinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = intent.getLongExtra("USER_ID", -1)

        lifecycleScope.launch {
            try {
                // Fetch medecin info
                val medecin = withContext(Dispatchers.IO) {
                    SupabaseClient.client.postgrest
                        .from("medecin")
                        .select {
                            filter {
                                eq("id_user", userId)
                            }
                        }
                        .decodeSingleOrNull<Medecin>()
                }

                // Fetch user info
                val user = withContext(Dispatchers.IO) {
                    SupabaseClient.client.postgrest
                        .from("utilisateur")
                        .select {
                            filter {
                                eq("id", userId)
                            }
                        }
                        .decodeSingleOrNull<Utilisateur>()
                }

                // Fetch last 5 patients
                val patients = withContext(Dispatchers.IO) {
                    SupabaseClient.client.postgrest
                        .from("patient")
                        .select {
                            order("id_patient", order = Order.DESCENDING)
                            limit(5)
                        }
                        .decodeList<Patient>()
                }

                // Update UI on Main Thread
                withContext(Dispatchers.Main) {
                    if (medecin != null) {
                        binding.tvWelcome.text = "Bienvenue Dr ${medecin.specialite}"
                        binding.tvSpecialite.text = medecin.specialite
                        binding.tvMatricule.text = "Matricule : ${medecin.matricule}"

                        if (user != null) {
                            binding.tvWelcome.text = "Dr ${user.nom} ${user.prenom}"
                        }
                    }

                    binding.rvRecentPatients.layoutManager = LinearLayoutManager(this@DashboardMedecinActivity)
                    binding.rvRecentPatients.adapter = PatientAdapter(patients)
                }

            } catch (e: Exception) {
                Log.e("DASHBOARD", "Global error: ${e.message}")
                try {
                     val patientsFallback = withContext(Dispatchers.IO) {
                        SupabaseClient.client.postgrest
                            .from("patient")
                            .select { limit(5) }
                            .decodeList<Patient>()
                    }
                    withContext(Dispatchers.Main) {
                        binding.rvRecentPatients.layoutManager = LinearLayoutManager(this@DashboardMedecinActivity)
                        binding.rvRecentPatients.adapter = PatientAdapter(patientsFallback)
                    }
                } catch (e2: Exception) {
                    Log.e("DASHBOARD", "Fallback error: ${e2.message}")
                }
            }
        }

        binding.cardNewPatient.setOnClickListener {
            startActivity(Intent(this, AddPatientActivity::class.java))
        }

        binding.cardConsultation.setOnClickListener {
            val intent = Intent(this, ConsultationActivity::class.java).apply {
                putExtra("USER_ID", userId)
            }
            startActivity(intent)
        }

        binding.cardHospitalisation.setOnClickListener {
            val intent = Intent(this, HospitalisationActivity::class.java).apply {
                putExtra("USER_ID", userId)
            }
            startActivity(intent)
        }

        binding.tvVoirTout.setOnClickListener {
            startActivity(Intent(this, AllPatientsActivity::class.java))
        }

        binding.cardPriseEnCharge.setOnClickListener {
            val intent = Intent(this, PriseEnChargeActivity::class.java).apply {
                putExtra("USER_ID", userId)
            }
            startActivity(intent)
        }

        binding.cardSearchPatient.setOnClickListener {
            startActivity(Intent(this, PatientSearchActivity::class.java))
        }
    }
}
