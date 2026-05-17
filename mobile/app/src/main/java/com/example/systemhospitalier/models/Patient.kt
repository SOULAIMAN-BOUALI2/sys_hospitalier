package com.example.systemhospitalier.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Patient(

    @SerialName("id_patient")
    val idPatient: Long,

    @SerialName("nom")
    val nom: String,

    @SerialName("prenom")
    val prenom: String,

    @SerialName("numero_dossier")
    val numeroDossier: String,

    @SerialName("date_naissance")
    val date: String,

    @SerialName("sexe")
    val sexe: String,

    @SerialName("adresse")
    val adresse: String,

    @SerialName("groupe_sanguin")
    val groupeSanguin: String,

    @SerialName("personne_urgence")
    val personneUrgence: String,

    @SerialName("telephone_urgence")
    val telephoneUrgence: String
)