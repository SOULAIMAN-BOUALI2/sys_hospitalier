package com.example.systemhospitalier.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.systemhospitalier.databinding.ItemPatientBinding
import com.example.systemhospitalier.models.Patient

class PatientAdapter(
    private val patients: List<Patient>
) : RecyclerView.Adapter<PatientAdapter.PatientViewHolder>() {

    class PatientViewHolder(
        val binding: ItemPatientBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PatientViewHolder {

        val binding = ItemPatientBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return PatientViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PatientViewHolder,
        position: Int
    ) {

        val patient = patients[position]

        holder.binding.tvNom.text =
            "${patient.nom} ${patient.prenom}"

        holder.binding.tvNumero.text =
            patient.numeroDossier
    }

    override fun getItemCount(): Int {
        return patients.size
    }
}