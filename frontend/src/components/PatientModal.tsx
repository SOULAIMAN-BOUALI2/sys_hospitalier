import React, { useEffect, useState } from "react";
import type { CSSProperties } from "react";
import type { Patient, PatientFormData } from "../types/patientTypes";

const GROUPES = ["A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"];

const initialForm: PatientFormData = {
  nom: "",
  prenom: "",
  numeroDossier: "",
  dateNaissance: "",
  sexe: "",
  adresse: "",
  telephone: "",
  groupeSanguin: "",
  personneUrgence: "",
  telephoneUrgence: "",
  medecinId: 0,
};

interface Props {
  patient: Patient | null;
  medecinId: number;
  onClose: () => void;
  onSave: (data: PatientFormData) => Promise<void>;
}

export default function PatientModal({ patient, medecinId, onClose, onSave }: Props) {
  const [form, setForm] = useState<PatientFormData>({ ...initialForm, medecinId });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    if (patient) {
      setForm({
        nom: patient.nom,
        prenom: patient.prenom,
        numeroDossier: patient.numeroDossier,
        dateNaissance: patient.dateNaissance || "",
        sexe: patient.sexe,
        adresse: patient.adresse || "",
        telephone: patient.telephone || "",
        groupeSanguin: patient.groupeSanguin || "",
        personneUrgence: patient.personneUrgence || "",
        telephoneUrgence: patient.telephoneUrgence || "",
        medecinId,
      });
    } else {
      setForm({ ...initialForm, medecinId });
    }
  }, [patient, medecinId]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    setForm({ ...form, [e.target.name]: e.target.value });
    setError("");
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!form.nom || !form.prenom || !form.numeroDossier) {
      setError("Nom, prénom et numéro de dossier sont obligatoires.");
      return;
    }
    setLoading(true);
    try {
      await onSave(form);
      onClose();
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : "Une erreur est survenue.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={styles.overlay} onClick={(e) => e.target === e.currentTarget && onClose()}>
      <div style={styles.modal}>
        <div style={styles.modalHeader}>
          <h2 style={styles.modalTitle}>
            {patient ? "Modifier le patient" : "Ajouter un patient"}
          </h2>
          <button style={styles.closeBtn} onClick={onClose}>✕</button>
        </div>

        {error && <div style={styles.errorBox}>{error}</div>}

        <form onSubmit={handleSubmit}>
          <div style={styles.grid}>
            <div style={styles.group}>
              <label style={styles.label}>Nom *</label>
              <input style={styles.input} name="nom" value={form.nom} onChange={handleChange} placeholder="Benali" />
            </div>
            <div style={styles.group}>
              <label style={styles.label}>Prénom *</label>
              <input style={styles.input} name="prenom" value={form.prenom} onChange={handleChange} placeholder="Ahmed" />
            </div>
            <div style={styles.group}>
              <label style={styles.label}>N° dossier *</label>
              <input style={styles.input} name="numeroDossier" value={form.numeroDossier} onChange={handleChange} placeholder="DOS-2024-001" />
            </div>
            <div style={styles.group}>
              <label style={styles.label}>Date de naissance</label>
              <input style={styles.input} type="date" name="dateNaissance" value={form.dateNaissance} onChange={handleChange} />
            </div>
            <div style={styles.group}>
              <label style={styles.label}>Sexe</label>
              <select style={styles.input} name="sexe" value={form.sexe} onChange={handleChange}>
                <option value="">-- Sélectionner --</option>
                <option value="M">Masculin</option>
                <option value="F">Féminin</option>
              </select>
            </div>
            <div style={styles.group}>
              <label style={styles.label}>Groupe sanguin</label>
              <select style={styles.input} name="groupeSanguin" value={form.groupeSanguin} onChange={handleChange}>
                <option value="">-- Sélectionner --</option>
                {GROUPES.map((g) => <option key={g}>{g}</option>)}
              </select>
            </div>
            <div style={styles.group}>
              <label style={styles.label}>Téléphone</label>
              <input style={styles.input} name="telephone" value={form.telephone} onChange={handleChange} placeholder="0612345678" />
            </div>
            <div style={{ ...styles.group, gridColumn: "1 / -1" }}>
              <label style={styles.label}>Adresse</label>
              <input style={styles.input} name="adresse" value={form.adresse} onChange={handleChange} placeholder="12 Rue Hassan II, Casablanca" />
            </div>
            <div style={styles.group}>
              <label style={styles.label}>Personne d'urgence</label>
              <input style={styles.input} name="personneUrgence" value={form.personneUrgence} onChange={handleChange} placeholder="Nom prénom" />
            </div>
            <div style={styles.group}>
              <label style={styles.label}>Tél. urgence</label>
              <input style={styles.input} name="telephoneUrgence" value={form.telephoneUrgence} onChange={handleChange} placeholder="0698765432" />
            </div>
          </div>

          <div style={styles.footer}>
            <button type="button" style={styles.cancelBtn} onClick={onClose}>Annuler</button>
            <button type="submit" style={styles.saveBtn} disabled={loading}>
              {loading ? "Enregistrement..." : "Enregistrer"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

const styles: Record<string, CSSProperties> = {
  overlay: {
    position: "fixed", inset: 0, background: "rgba(0,0,0,0.4)",
    display: "flex", alignItems: "center", justifyContent: "center", zIndex: 1000,
  },
  modal: {
    background: "#fff", borderRadius: 12, padding: "1.5rem",
    width: 520, maxWidth: "95vw", maxHeight: "90vh", overflowY: "auto",
    boxShadow: "0 8px 32px rgba(0,0,0,0.18)",
  },
  modalHeader: { display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "1rem" },
  modalTitle: { fontSize: 17, fontWeight: 600, color: "#111" },
  closeBtn: { background: "none", border: "none", fontSize: 18, cursor: "pointer", color: "#888" },
  errorBox: { background: "#FCEBEB", color: "#A32D2D", borderRadius: 8, padding: "8px 12px", fontSize: 13, marginBottom: 12 },
  grid: { display: "grid", gridTemplateColumns: "1fr 1fr", gap: 12 },
  group: { display: "flex", flexDirection: "column", gap: 4 },
  label: { fontSize: 12, color: "#666", fontWeight: 500 },
  input: {
    padding: "8px 10px", border: "1px solid #ddd", borderRadius: 8,
    fontSize: 13, outline: "none", color: "#111", background: "#fafafa",
  },
  footer: { display: "flex", justifyContent: "flex-end", gap: 10, marginTop: "1.25rem" },
  cancelBtn: {
    background: "none", border: "1px solid #ddd", borderRadius: 8,
    padding: "8px 18px", fontSize: 13, cursor: "pointer", color: "#555",
  },
  saveBtn: {
    background: "#185FA5", color: "#fff", border: "none",
    borderRadius: 8, padding: "8px 20px", fontSize: 13, cursor: "pointer",
  },
};