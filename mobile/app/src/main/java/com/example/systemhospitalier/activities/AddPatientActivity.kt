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

            val numero =
                binding.etNumeroDossier.text.toString()

            val nom =
                binding.etNom.text.toString()

            val prenom =
                binding.etPrenom.text.toString()

            val date =
                binding.etDateNaissance.text.toString()

            val adresse =
                binding.etAdresse.text.toString()

            val urgence =
                binding.etPersonneUrgence.text.toString()

            val telephone =
                binding.etTelephoneUrgence.text.toString()

            //au lieu de faire .log -> insertion dans la bd
            lifecycleScope.launch {

                try {

                    SupabaseClient.client.postgrest
                        .from("patient")
                        .insert(
                            Patient(
                                idPatient = 0,
                                nom = nom,
                                prenom = prenom,
                                numeroDossier = numero,
                                date = date,
                                sexe = binding.autoSexe.text.toString(),
                                adresse = adresse,
                                groupeSanguin = binding.autoGroupeSanguin.text.toString(),
                                personneUrgence = urgence,
                                telephoneUrgence = telephone
                            )
                        )

                    Toast.makeText(
                        this@AddPatientActivity,
                        "Patient ajouté",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()

                } catch (e: Exception) {

                    Log.e("SUPABASE", e.message.toString())
                }
            }

        }
    }
}