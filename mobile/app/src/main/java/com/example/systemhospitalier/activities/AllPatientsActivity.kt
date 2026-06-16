package com.example.systemhospitalier.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.systemhospitalier.adapter.PatientAdapter
import com.example.systemhospitalier.databinding.ActivityAllPatientsBinding
import com.example.systemhospitalier.models.Patient
import com.example.systemhospitalier.network.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AllPatientsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAllPatientsBinding
    private var allPatients: List<Patient> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAllPatientsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.rvPatients.layoutManager = LinearLayoutManager(this)

        loadPatients()
        setupSearch()
    }

    private fun loadPatients() {
        lifecycleScope.launch {
            try {
                val patients = withContext(Dispatchers.IO) {
                    SupabaseClient.client.postgrest
                        .from("patient")
                        .select()
                        .decodeList<Patient>()
                }

                allPatients = patients
                withContext(Dispatchers.Main) {
                    binding.rvPatients.adapter = PatientAdapter(allPatients)
                }

            } catch (e: Exception) {
                Log.e("SUPABASE_ERROR", "Error loading patients: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AllPatientsActivity, "Erreur de chargement", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().lowercase().trim()
                val filteredList = if (query.isEmpty()) {
                    allPatients
                } else {
                    allPatients.filter {
                        it.nom.lowercase().contains(query) ||
                        it.prenom.lowercase().contains(query) ||
                        it.numeroDossier.lowercase().contains(query)
                    }
                }
                binding.rvPatients.adapter = PatientAdapter(filteredList)
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }
}
