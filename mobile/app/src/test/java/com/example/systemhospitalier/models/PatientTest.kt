package com.example.systemhospitalier.models

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PatientTest {

    @Test
    fun `test patient creation and attributes`() {
        val patient = Patient(
            idPatient = 1L,
            nom = "BOUALI",
            prenom = "Soulaiman",
            numeroDossier = "D12345",
            date = "1990-01-01",
            sexe = "M",
            adresse = "Rabat",
            groupeSanguin = "O+",
            personneUrgence = "Contact",
            telephoneUrgence = "0600000000"
        )

        assertEquals(1L, patient.idPatient)
        assertEquals("BOUALI", patient.nom)
        assertEquals("Soulaiman", patient.prenom)
        assertEquals("D12345", patient.numeroDossier)
        assertEquals("1990-01-01", patient.date)
        assertEquals("M", patient.sexe)
        assertEquals("Rabat", patient.adresse)
        assertEquals("O+", patient.groupeSanguin)
        assertEquals("Contact", patient.personneUrgence)
        assertEquals("0600000000", patient.telephoneUrgence)
    }

    @Test
    fun `test patient attributes not null`() {
        val patient = Patient(
            idPatient = 2L,
            nom = "TEST",
            prenom = "User",
            numeroDossier = "T999",
            date = "2000-10-10",
            sexe = "F",
            adresse = "Casablanca",
            groupeSanguin = "A-",
            personneUrgence = "Emergency",
            telephoneUrgence = "0611111111"
        )

        assertNotNull(patient.nom)
        assertNotNull(patient.prenom)
        assertNotNull(patient.numeroDossier)
        assertNotNull(patient.date)
    }
}
