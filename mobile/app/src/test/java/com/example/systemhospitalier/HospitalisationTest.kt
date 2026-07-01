package com.example.systemhospitalier

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class HospitalisationTest {

    @Test
    fun `test hospitalisation creation and attributes`() {
        val hospitalisation = Hospitalisation(
            idHosp = 100L,
            dateAdmission = "2026-06-10",
            dateSortie = null,
            chambre = "Room 302",
            motif = "Opération",
            etatPatient = "Stable",
            diagnosticInitial = "Fracture",
            idPatient = 1L,
            idMedecin = 5L
        )

        assertEquals(100L, hospitalisation.idHosp)
        assertEquals("2026-06-10", hospitalisation.dateAdmission)
        assertNull(hospitalisation.dateSortie)
        assertEquals("Room 302", hospitalisation.chambre)
        assertEquals("Stable", hospitalisation.etatPatient)
        assertEquals(1L, hospitalisation.idPatient)
    }

    @Test
    fun `test hospitalisation with sortie date`() {
        val hospitalisation = Hospitalisation(
            idHosp = 101L,
            dateAdmission = "2026-06-01",
            dateSortie = "2026-06-05",
            chambre = "Room 101",
            motif = "Observation",
            etatPatient = "Sortie",
            diagnosticInitial = "Malaise",
            idPatient = 2L,
            idMedecin = 6L
        )

        assertNotNull(hospitalisation.dateSortie)
        assertEquals("2026-06-05", hospitalisation.dateSortie)
    }
}
