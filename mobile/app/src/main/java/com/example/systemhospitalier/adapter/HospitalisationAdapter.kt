package com.example.systemhospitalier.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.systemhospitalier.Hospitalisation
import com.example.systemhospitalier.databinding.ItemConsultationBinding

class HospitalisationAdapter(
    private val list: List<Hospitalisation>,
    private val onClick: (Hospitalisation) -> Unit
) : RecyclerView.Adapter<HospitalisationAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemConsultationBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemConsultationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.binding.apply {
            tvDate.text = "Admission le ${item.dateAdmission}"
            tvMotif.text = "Motif: ${item.motif}"
            root.setOnClickListener { onClick(item) }
        }
    }

    override fun getItemCount(): Int = list.size
}
