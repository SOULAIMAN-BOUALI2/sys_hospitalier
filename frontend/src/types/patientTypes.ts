export interface Patient {
  idPatient: number;
  nom: string;
  prenom: string;
  numeroDossier: string;
  dateNaissance: string;
  sexe: "M" | "F" | "";
  adresse: string;
  telephone: string;
  groupeSanguin: string;
  personneUrgence: string;
  telephoneUrgence: string;
}

export interface PatientFormData {
  nom: string;
  prenom: string;
  numeroDossier: string;
  dateNaissance: string;
  sexe: "M" | "F" | "";
  adresse: string;
  telephone: string;
  groupeSanguin: string;
  personneUrgence: string;
  telephoneUrgence: string;
  medecinId?: number; // optionnel, plus utilisé
}