package com.example.systemhospitalier.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.systemhospitalier.EtapePriseEnCharge
import com.example.systemhospitalier.databinding.ItemEtapeBinding

class EtapeAdapter(
    private var etapes: List<EtapePriseEnCharge>,
    private val onStatusChanged: ((EtapePriseEnCharge, Boolean) -> Unit)? = null
) : RecyclerView.Adapter<EtapeAdapter.EtapeViewHolder>() {

    class EtapeViewHolder(val binding: ItemEtapeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EtapeViewHolder {
        val binding = ItemEtapeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EtapeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EtapeViewHolder, position: Int) {
        val etape = etapes[position]
        holder.binding.apply {
            tvOrdre.text = etape.ordre.toString()
            tvType.text = etape.type ?: "Action"
            tvDescription.text = etape.description
            tvEtat.text = etape.etat
            tvActeur.text = "Acteur: ${etape.acteur ?: "Non spécifié"}"
            
            // Afficher la CheckBox si on est dans l'activité de suivi
            if (onStatusChanged != null) {
                cbTermine.visibility = View.VISIBLE
                cbTermine.isChecked = etape.etat == "Terminé"
                cbTermine.setOnCheckedChangeListener { _, isChecked ->
                    onStatusChanged.invoke(etape, isChecked)
                }
            } else {
                cbTermine.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int = etapes.size

    fun updateData(newEtapes: List<EtapePriseEnCharge>) {
        this.etapes = newEtapes
        notifyDataSetChanged()
    }
}
