package com.example.systemhospitalier.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.systemhospitalier.databinding.ItemPatientSearchBinding
import com.example.systemhospitalier.models.Patient

class PatientSearchAdapter(
    private var patients: List<Patient>,
    private val onPatientClick: (Patient) -> Unit
) : RecyclerView.Adapter<PatientSearchAdapter.PatientViewHolder>() {

    class PatientViewHolder(val binding: ItemPatientSearchBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientViewHolder {
        val binding = ItemPatientSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PatientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PatientViewHolder, position: Int) {
        val patient = patients[position]
        holder.binding.apply {
            tvFullNom.text = "${patient.nom} ${patient.prenom}"
            tvNumeroDossier.text = "N° Dossier: ${patient.numeroDossier}"
            root.setOnClickListener { onPatientClick(patient) }
        }
    }

    override fun getItemCount(): Int = patients.size

    fun updateList(newList: List<Patient>) {
        patients = newList
        notifyDataSetChanged()
    }
}
