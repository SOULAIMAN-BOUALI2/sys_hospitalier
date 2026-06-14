package com.example.systemhospitalier.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.systemhospitalier.Consultation
import com.example.systemhospitalier.databinding.ItemConsultationBinding

class ConsultationAdapter(
    private val list: List<Consultation>,
    private val onClick: (Consultation) -> Unit
) : RecyclerView.Adapter<ConsultationAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemConsultationBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemConsultationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.binding.apply {
            tvDate.text = "Le ${item.dateConsultation}"
            tvMotif.text = "Motif: ${item.motif}"
            root.setOnClickListener { onClick(item) }
        }
    }

    override fun getItemCount(): Int = list.size
}
