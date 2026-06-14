package com.example.systemhospitalier.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.systemhospitalier.PriseEnCharge
import com.example.systemhospitalier.databinding.ItemPriseEnChargeBinding

class PriseEnChargeAdapter(
    private val items: List<PriseEnCharge>,
    private val onItemClick: (PriseEnCharge) -> Unit
) : RecyclerView.Adapter<PriseEnChargeAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemPriseEnChargeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPriseEnChargeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.tvPatientId.text = "Patient ID: ${item.patientId}"
        holder.binding.tvDate.text = "Le ${item.dateDebut ?: "Date inconnue"}"
        holder.binding.tvSymptomes.text = "Symptômes: ${item.symptomes}"
        
        holder.itemView.setOnClickListener { onItemClick(item) }
    }

    override fun getItemCount(): Int = items.size
}
