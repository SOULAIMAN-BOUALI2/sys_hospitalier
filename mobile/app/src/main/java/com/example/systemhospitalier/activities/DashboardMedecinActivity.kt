package com.example.systemhospitalier.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.systemhospitalier.activities.AddPatientActivity
import com.example.systemhospitalier.Medecin
import com.example.systemhospitalier.network.SupabaseClient
import com.example.systemhospitalier.Utilisateur
import com.example.systemhospitalier.databinding.ActivityDashboardMedecinBinding
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class DashboardMedecinActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardMedecinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardMedecinBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val userId = intent.getLongExtra("USER_ID", -1)

        lifecycleScope.launch {

            val medecin = SupabaseClient.client.postgrest
                .from("medecin")
                .select {
                    filter {
                        eq("id_user", userId)
                    }
                }
                .decodeSingleOrNull<Medecin>()

            val user = SupabaseClient.client.postgrest
                .from("utilisateur")
                .select {
                    filter {
                        eq("id", userId)
                    }
                }
                .decodeSingleOrNull<Utilisateur>()

            runOnUiThread {

                if (medecin != null) {
                    Toast.makeText(
                        this@DashboardMedecinActivity,
                        medecin.specialite,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            if (medecin != null) {

                binding.tvWelcome.text =
                    "Bienvenue Dr ${medecin.specialite}"

                if (user != null && medecin != null) {

                    binding.tvWelcome.text =
                        "Dr ${user.nom} ${user.prenom}"

                    binding.tvSpecialite.text =
                        medecin.specialite

                    binding.tvMatricule.text =
                        "Matricule : ${medecin.matricule}"
                }
            }
        }
        binding.cardNewPatient.setOnClickListener {

            val intent = Intent(
                this,
                AddPatientActivity::class.java
            )

            startActivity(intent)
        }
    }
}