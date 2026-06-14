package com.example.systemhospitalier.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.systemhospitalier.PriseEnCharge
import com.example.systemhospitalier.adapter.PriseEnChargeAdapter
import com.example.systemhospitalier.databinding.ActivityGenericListBinding
import com.example.systemhospitalier.network.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PatientPriseEnChargeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGenericListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenericListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }
        supportActionBar?.title = "Prises en charge du Patient"

        val patientId = intent.getLongExtra("PATIENT_ID", -1)
        if (patientId != -1L) {
            loadPrisesEnCharge(patientId)
        }
    }

    private fun loadPrisesEnCharge(patientId: Long) {
        lifecycleScope.launch {
            binding.progressBar.visibility = View.VISIBLE
            try {
                val list = withContext(Dispatchers.IO) {
                    SupabaseClient.client.postgrest
                        .from("prise_en_charge")
                        .select {
                            filter { eq("patient_id", patientId) }
                        }
                        .decodeList<PriseEnCharge>()
                }

                binding.rvList.layoutManager = LinearLayoutManager(this@PatientPriseEnChargeActivity)
                binding.rvList.adapter = PriseEnChargeAdapter(list) { item ->
                    val intent = Intent(this@PatientPriseEnChargeActivity, PriseEnChargeDetailActivity::class.java)
                    intent.putExtra("PEC_JSON", Json.encodeToString(item))
                    startActivity(intent)
                }

                if (list.isEmpty()) {
                    binding.tvEmpty.visibility = View.VISIBLE
                    binding.tvEmpty.text = "Aucune prise en charge trouvée"
                }

            } catch (e: Exception) {
                Toast.makeText(this@PatientPriseEnChargeActivity, "Erreur: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }
}
