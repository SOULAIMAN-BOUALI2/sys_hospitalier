package com.example.systemhospitalier.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.systemhospitalier.EtapePriseEnCharge
import com.example.systemhospitalier.PriseEnCharge
import com.example.systemhospitalier.adapter.EtapeAdapter
import com.example.systemhospitalier.adapter.PriseEnChargeAdapter
import com.example.systemhospitalier.databinding.ActivityListPriseEnChargeBinding
import com.example.systemhospitalier.network.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListPriseEnChargeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListPriseEnChargeBinding
    private lateinit var etapeAdapter: EtapeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListPriseEnChargeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        loadPrisesEnCharge()
    }

    private fun loadPrisesEnCharge() {
        lifecycleScope.launch {
            binding.progressBar.visibility = View.VISIBLE
            try {
                val list = withContext(Dispatchers.IO) {
                    SupabaseClient.client.postgrest
                        .from("prise_en_charge")
                        .select()
                        .decodeList<PriseEnCharge>()
                }

                binding.rvPrisesEnCharge.layoutManager = LinearLayoutManager(this@ListPriseEnChargeActivity)
                binding.rvPrisesEnCharge.adapter = PriseEnChargeAdapter(list) { pec ->
                    loadEtapesForPEC(pec.idPriseEnCharge!!)
                }

            } catch (e: Exception) {
                Log.e("LIST_PEC", "Error: ${e.message}")
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun loadEtapesForPEC(pecId: Long) {
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

                binding.tvStepsTitle.visibility = View.VISIBLE
                binding.rvEtapesSuivi.visibility = View.VISIBLE
                
                etapeAdapter = EtapeAdapter(steps) { etape, isChecked ->
                    updateEtapeStatus(etape, isChecked)
                }
                binding.rvEtapesSuivi.layoutManager = LinearLayoutManager(this@ListPriseEnChargeActivity)
                binding.rvEtapesSuivi.adapter = etapeAdapter

            } catch (e: Exception) {
                Log.e("LIST_STEPS", "Error: ${e.message}")
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
                Toast.makeText(this@ListPriseEnChargeActivity, "Statut mis à jour", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("UPDATE_STEP", "Error: ${e.message}")
            }
        }
    }
}
