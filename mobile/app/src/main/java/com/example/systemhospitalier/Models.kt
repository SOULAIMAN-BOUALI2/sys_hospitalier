package com.example.systemhospitalier

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Utilisateur(
    @SerialName("id") val id: Long = 0,
    @SerialName("nom") val nom: String,
    @SerialName("prenom") val prenom: String,
    @SerialName("email") val email: String,
    @SerialName("mot_de_passe") val motDePasse: String,
    @SerialName("date_creation") val dateCreation: String? = null,
    @SerialName("statut_compte") val statutCompte: String? = "actif"
)

@Serializable
data class Medecin(
    @SerialName("id_medecin") val idMedecin: Long = 0,
    @SerialName("matricule") val matricule: String,
    @SerialName("specialite") val specialite: String? = null,
    @SerialName("disponibilite") val disponibilite: Boolean? = true,
    @SerialName("id_user") val idUser: Long
)

@Serializable
data class Infirmier(
    @SerialName("id_infirmier") val idInfirmier: Long = 0,
    @SerialName("matricule") val matricule: String,
    @SerialName("service") val service: String? = null,
    @SerialName("shift") val shift: String? = null,
    @SerialName("id_user") val idUser: Long
)

@Serializable
data class Consultation(
    @SerialName("id") val id: Long = 0,
    @SerialName("patient_id") val patientId: Long,
    @SerialName("medecin_id") val medecinId: Long,
    @SerialName("date_consultation") val date: String,
    @SerialName("motif") val motif: String,
    @SerialName("diagnostic") val diagnostic: String,
    @SerialName("traitement") val traitement: String,
    @SerialName("notes") val notes: String? = null
)
