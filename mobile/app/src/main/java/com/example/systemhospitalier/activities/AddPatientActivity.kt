package com.example.systemhospitalier.activities

import android.R
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.systemhospitalier.databinding.ActivityAddPatientBinding
import android.app.DatePickerDialog
import android.widget.ArrayAdapter
import java.util.Calendar
import androidx.lifecycle.lifecycleScope
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch
import android.widget.Toast
import com.example.systemhospitalier.network.SupabaseClient
import com.example.systemhospitalier.models.Patient
class AddPatientActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPatientBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddPatientBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.etDateNaissance.setOnClickListener {

            val calendar = Calendar.getInstance()

            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(
                this,
                { _, y, m, d ->

                    binding.etDateNaissance.setText(
                        String.format("%04d-%02d-%02d", y, m + 1, d)
                    )
                },
                year,
                month,
                day
            )

            datePicker.show()
        }
        val sexes = listOf(
            "HOMME",
            "FEMME"
        )

        val sexeAdapter = ArrayAdapter(
            this,
            R.layout.simple_dropdown_item_1line,
            sexes
        )

        binding.autoSexe.setAdapter(sexeAdapter)

        val groupes = listOf(
            "A+",
            "A-",
            "B+",
            "B-",
            "AB+",
            "AB-",
            "O+",
            "O-"
        )

        val groupeAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            groupes
        )

        binding.autoGroupeSanguin.setAdapter(groupeAdapter)

        binding.btnSavePatient.setOnClickListener {

            val numero = binding.etNumeroDossier.text.toString().trim()
            val nom = binding.etNom.text.toString().trim()
            val prenom = binding.etPrenom.text.toString().trim()
            val date = binding.etDateNaissance.text.toString().trim()
            val adresse = binding.etAdresse.text.toString().trim()
            val urgence = binding.etPersonneUrgence.text.toString().trim()
            val telephone = binding.etTelephoneUrgence.text.toString().trim()
            val sexe = binding.autoSexe.text.toString().trim()
            val groupeSanguin = binding.autoGroupeSanguin.text.toString().trim()

            if (numero.isEmpty() || nom.isEmpty() || prenom.isEmpty() || date.isEmpty() || sexe.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir les champs obligatoires", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    SupabaseClient.client.postgrest
                        .from("patient")
                        .insert(
                            Patient(
                                nom = nom,
                                prenom = prenom,
                                numeroDossier = numero,
                                date = date,
                                sexe = sexe,
                                adresse = adresse,
                                groupeSanguin = groupeSanguin,
                                personneUrgence = urgence,
                                telephoneUrgence = telephone
                            )
                        )

                    Toast.makeText(
                        this@AddPatientActivity,
                        "Patient ajouté avec succès",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()

                } catch (e: Exception) {
                    Log.e("SUPABASE", "Error inserting patient: ${e.message}")
                    Toast.makeText(
                        this@AddPatientActivity,
                        "Erreur lors de l'ajout : ${e.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}