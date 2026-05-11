import type { Patient, PatientFormData } from "../types/patientTypes";

const API_BASE = "http://localhost:8080/api/medecin";

const getAuthHeaders = (): Record<string, string> => ({
  "Content-Type": "application/json",
  "Authorization": `Bearer ${localStorage.getItem("token") ?? ""}`,
});

export const patientService = {
  // GET tous les patients
  getPatients: async (): Promise<Patient[]> => {
    const res = await fetch(`${API_BASE}/patients`, {
      headers: getAuthHeaders(),
    });
    if (!res.ok) throw new Error("Erreur lors de la récupération des patients");
    return res.json();
  },

  // GET un patient par ID
  getPatient: async (id: number): Promise<Patient> => {
    const res = await fetch(`${API_BASE}/patients/${id}`, {
      headers: getAuthHeaders(),
    });
    if (!res.ok) throw new Error("Patient non trouvé");
    return res.json();
  },

  // POST créer un patient
  createPatient: async (data: PatientFormData): Promise<Patient> => {
    const res = await fetch(`${API_BASE}/patients`, {
      method: "POST",
      headers: getAuthHeaders(),
      body: JSON.stringify(data),
    });
    if (!res.ok) throw new Error("Erreur lors de la création du patient");
    return res.json();
  },

  // PUT modifier un patient
  updatePatient: async (id: number, data: PatientFormData): Promise<Patient> => {
    const res = await fetch(`${API_BASE}/patients/${id}`, {
      method: "PUT",
      headers: getAuthHeaders(),
      body: JSON.stringify(data),
    });
    if (!res.ok) throw new Error("Erreur lors de la modification du patient");
    return res.json();
  },

  // DELETE supprimer un patient
  deletePatient: async (id: number): Promise<void> => {
    const res = await fetch(`${API_BASE}/patients/${id}`, {
      method: "DELETE",
      headers: getAuthHeaders(),
    });
    if (!res.ok) throw new Error("Erreur lors de la suppression du patient");
  },
};