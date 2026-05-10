package com.example.systemhospitalier.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.systemhospitalier.R
import com.example.systemhospitalier.models.Patient
import com.example.systemhospitalier.network.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.systemhospitalier.adapter.PatientAdapter
import com.example.systemhospitalier.databinding.ActivityAllPatientsBinding
class AllPatientsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAllPatientsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAllPatientsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        lifecycleScope.launch {

            try {

                val patients = SupabaseClient.client.postgrest
                    .from("patient")
                    .select()
                    .decodeList<Patient>()

                Log.d("PATIENTS", patients.toString())

                binding.rvPatients.layoutManager =
                    LinearLayoutManager(this@AllPatientsActivity)

                binding.rvPatients.adapter =
                    PatientAdapter(patients)

            } catch (e: Exception) {

                Log.e("SUPABASE_ERROR", e.message.toString())
            }
        }
    }
}