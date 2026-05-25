export interface Hospitalisation {
  idHosp: number;
  patientId: number;
  patientNomComplet: string;
  medecinId: number;
  medecinNomComplet: string;
  dateAdmission: string;
  dateSortie: string | null;
  chambre: string;
  motif: string;
  etatPatient: string;
  diagnosticInitial: string;
}

export interface HospitalisationFormData {
  patientId: number;
  medecinId: number;
  dateAdmission: string;
  dateSortie?: string;
  chambre: string;
  motif: string;
  etatPatient: string;
  diagnosticInitial: string;
}