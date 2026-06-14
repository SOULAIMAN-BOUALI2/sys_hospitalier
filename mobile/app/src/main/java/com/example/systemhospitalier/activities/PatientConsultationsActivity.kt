package com.example.systemhospitalier.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.systemhospitalier.Consultation
import com.example.systemhospitalier.adapter.ConsultationAdapter
import com.example.systemhospitalier.databinding.ActivityGenericListBinding
import com.example.systemhospitalier.network.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PatientConsultationsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGenericListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenericListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }
        supportActionBar?.title = "Consultations du Patient"

        val patientId = intent.getLongExtra("PATIENT_ID", -1)
        if (patientId != -1L) {
            loadConsultations(patientId)
        }
    }

    private fun loadConsultations(patientId: Long) {
        lifecycleScope.launch {
            binding.progressBar.visibility = View.VISIBLE
            try {
                val list = withContext(Dispatchers.IO) {
                    SupabaseClient.client.postgrest
                        .from("consultation")
                        .select {
                            filter { eq("patient_id", patientId) }
                        }
                        .decodeList<Consultation>()
                }

                binding.rvList.layoutManager = LinearLayoutManager(this@PatientConsultationsActivity)
                binding.rvList.adapter = ConsultationAdapter(list) { consultation ->
                    val intent = Intent(this@PatientConsultationsActivity, ConsultationDetailActivity::class.java)
                    intent.putExtra("CONSULTATION_JSON", Json.encodeToString(consultation))
                    startActivity(intent)
                }

                if (list.isEmpty()) {
                    binding.tvEmpty.visibility = View.VISIBLE
                    binding.tvEmpty.text = "Aucune consultation trouvée"
                }

            } catch (e: Exception) {
                Toast.makeText(this@PatientConsultationsActivity, "Erreur: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }
}
