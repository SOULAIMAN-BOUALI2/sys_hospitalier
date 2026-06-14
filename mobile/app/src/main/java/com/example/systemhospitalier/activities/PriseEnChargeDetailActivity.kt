package com.example.systemhospitalier.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.systemhospitalier.EtapePriseEnCharge
import com.example.systemhospitalier.PriseEnCharge
import com.example.systemhospitalier.adapter.EtapeAdapter
import com.example.systemhospitalier.databinding.ActivityPriseEnChargeDetailBinding
import com.example.systemhospitalier.network.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class PriseEnChargeDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPriseEnChargeDetailBinding
    private lateinit var etapeAdapter: EtapeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPriseEnChargeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        val jsonString = intent.getStringExtra("PEC_JSON")
        if (jsonString != null) {
            val pec = Json.decodeFromString<PriseEnCharge>(jsonString)
            displayPecInfo(pec)
            loadEtapes(pec.idPriseEnCharge!!)
        }
    }

    private fun displayPecInfo(item: PriseEnCharge) {
        addDetailRow("Date de début", item.dateDebut ?: "Non spécifiée")
        addDetailRow("Symptômes", item.symptomes)
        addDetailRow("Constantes", item.constantes)
        addDetailRow("Niveau d'urgence", item.niveauUrgence)
        addDetailRow("État global", item.etat ?: "En cours")
        addDetailRow("Observation", item.observation)
    }

    private fun addDetailRow(label: String, value: String) {
        val textView = TextView(this)
        textView.text = "$label :\n$value\n"
        textView.textSize = 14f
        textView.setPadding(0, 8, 0, 8)
        binding.pecInfoContainer.addView(textView)
    }

    private fun loadEtapes(pecId: Long) {
        lifecycleScope.launch {
            binding.progressBar.visibility = View.VISIBLE
            try {
                val steps = withContext(Dispatchers.IO) {
                    SupabaseClient.client.postgrest
                        .from("etape_prise_en_charge")
                        .select { filter { eq("prise_en_charge_id", pecId) } }
                        .decodeList<EtapePriseEnCharge>()
                        .sortedBy { it.ordre }
                }

                etapeAdapter = EtapeAdapter(steps) { etape, isChecked ->
                    updateEtapeStatus(etape, isChecked)
                }
                binding.rvEtapes.layoutManager = LinearLayoutManager(this@PriseEnChargeDetailActivity)
                binding.rvEtapes.adapter = etapeAdapter

            } catch (e: Exception) {
                Log.e("PEC_DETAIL", "Error loading steps: ${e.message}")
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun updateEtapeStatus(etape: EtapePriseEnCharge, isChecked: Boolean) {
        val newStatus = if (isChecked) "Terminé" else "A faire"
        lifecycleScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    SupabaseClient.client.postgrest
                        .from("etape_prise_en_charge")
                        .update({
                            set("etat", newStatus)
                            if (isChecked) {
                                val now = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())
                                set("date_fin", now)
                            } else {
                                set("date_fin", null as String?)
                            }
                        }) {
                            filter { eq("id_etape", etape.idEtape!!) }
                        }
                }
                Toast.makeText(this@PriseEnChargeDetailActivity, "Statut mis à jour", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("UPDATE_STEP", "Error: ${e.message}")
            }
        }
    }
}
