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
    @SerialName("id_consultation") val idConsultation: Long = 0,
    @SerialName("patient_id") val patientId: Long,
    @SerialName("medecin_id") val medecinId: Long,
    @SerialName("date_consultation") val dateConsultation: String,
    @SerialName("motif") val motif: String,
    @SerialName("diagnostic") val diagnostic: String,
    @SerialName("traitement") val traitement: String,
    @SerialName("notes") val notes: String? = null
)

@Serializable
data class Hospitalisation(
    @SerialName("id_hosp") val idHosp: Long = 0,
    @SerialName("date_admission") val dateAdmission: String,
    @SerialName("date_sortie") val dateSortie: String? = null,
    @SerialName("chambre") val chambre: String,
    @SerialName("motif") val motif: String,
    @SerialName("etat_patient") val etatPatient: String,
    @SerialName("diagnostic_initial") val diagnosticInitial: String,
    @SerialName("id_patient") val idPatient: Long,
    @SerialName("id_medecin") val idMedecin: Long
)

@Serializable
data class PriseEnCharge(
    @SerialName("id_prise_en_charge") val idPriseEnCharge: Long? = null,
    @SerialName("patient_id") val patientId: Long,
    @SerialName("medecin_id") val medecinId: Long,
    @SerialName("date_debut") val dateDebut: String? = null,
    @SerialName("type") val type: String? = "Standard",
    @SerialName("etat") val etat: String? = "En cours",
    @SerialName("symptomes") val symptomes: String,
    @SerialName("constantes") val constantes: String,
    @SerialName("observation") val observation: String,
    @SerialName("niveau_urgence") val niveauUrgence: String,
    @SerialName("recommandations_ia") val recommandationsIA: String? = null
)

@Serializable
data class EtapePriseEnCharge(
    @SerialName("id_etape") val idEtape: Long? = null,
    @SerialName("prise_en_charge_id") val priseEnChargeId: Long,
    @SerialName("ordre") val ordre: Int,
    @SerialName("type") val type: String? = null,
    @SerialName("description") val description: String,
    @SerialName("date_debut") val dateDebut: String? = null,
    @SerialName("date_fin") val dateFin: String? = null,
    @SerialName("etat") val etat: String? = "A faire",
    @SerialName("observation") val observation: String? = null,
    @SerialName("acteur") val acteur: String? = null,
    @SerialName("est_approuve") val estApprouve: Boolean = false
)
