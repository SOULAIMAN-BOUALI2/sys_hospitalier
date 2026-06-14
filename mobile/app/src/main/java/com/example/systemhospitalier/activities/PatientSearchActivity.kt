package com.example.systemhospitalier.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.systemhospitalier.adapter.PatientSearchAdapter
import com.example.systemhospitalier.databinding.ActivityPatientSearchBinding
import com.example.systemhospitalier.models.Patient
import com.example.systemhospitalier.network.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PatientSearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPatientSearchBinding
    private var allPatients: List<Patient> = emptyList()
    private lateinit var adapter: PatientSearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        setupRecyclerView()
        loadPatients()
        setupSearch()
    }

    private fun setupRecyclerView() {
        adapter = PatientSearchAdapter(emptyList()) { patient ->
            Toast.makeText(this, "Patient: ${patient.nom} ${patient.prenom}\nN° Dossier: ${patient.numeroDossier}", Toast.LENGTH_LONG).show()
            // Plus tard: ouvrir l'activité de détails
        }
        binding.rvPatients.adapter = adapter
    }

    private fun loadPatients() {
        lifecycleScope.launch {
            binding.progressBar.visibility = View.VISIBLE
            try {
                allPatients = withContext(Dispatchers.IO) {
                    SupabaseClient.client.postgrest
                        .from("patient")
                        .select()
                        .decodeList<Patient>()
                }
                withContext(Dispatchers.Main) {
                    adapter.updateList(allPatients)
                    updateNoResultView(allPatients.isEmpty())
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@PatientSearchActivity, "Erreur de chargement des patients", Toast.LENGTH_SHORT).show()
                }
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filter(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun filter(text: String) {
        val filteredList = allPatients.filter { patient ->
            patient.nom.contains(text, ignoreCase = true) ||
            patient.prenom.contains(text, ignoreCase = true) ||
            patient.numeroDossier.contains(text, ignoreCase = true)
        }
        adapter.updateList(filteredList)
        updateNoResultView(filteredList.isEmpty())
    }

    private fun updateNoResultView(isEmpty: Boolean) {
        binding.tvNoResult.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }
}
