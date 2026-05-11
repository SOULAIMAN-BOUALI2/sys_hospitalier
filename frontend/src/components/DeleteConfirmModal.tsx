import React from "react";
import type { CSSProperties } from "react";

interface Patient {
  idPatient: number;
  nom: string;
  prenom: string;
  numeroDossier: string;
}

interface Props {
  patient: Patient | null;
  loading: boolean;
  onClose: () => void;
  onConfirm: () => void;
}

export default function DeleteConfirmModal({ patient, loading, onClose, onConfirm }: Props) {
  if (!patient) return null;

  return (
    <div style={styles.overlay} onClick={(e) => e.target === e.currentTarget && onClose()}>
      <div style={styles.modal}>
        <div style={styles.icon}>🗑️</div>
        <h2 style={styles.title}>Supprimer le patient ?</h2>
        <p style={styles.msg}>
          Vous êtes sur le point de supprimer{" "}
          <strong>{patient.nom} {patient.prenom}</strong> ({patient.numeroDossier}).
          <br />Cette action est irréversible.
        </p>
        <div style={styles.footer}>
          <button style={styles.cancelBtn} onClick={onClose}>Annuler</button>
          <button style={styles.delBtn} onClick={onConfirm} disabled={loading}>
            {loading ? "Suppression..." : "Supprimer"}
          </button>
        </div>
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
    background: "#fff", borderRadius: 12, padding: "2rem",
    width: 380, textAlign: "center", boxShadow: "0 8px 32px rgba(0,0,0,0.18)",
  },
  icon: { fontSize: 36, marginBottom: 12 },
  title: { fontSize: 17, fontWeight: 600, color: "#111", marginBottom: 8 },
  msg: { fontSize: 14, color: "#555", lineHeight: 1.6, marginBottom: "1.5rem" },
  footer: { display: "flex", gap: 10, justifyContent: "center" },
  cancelBtn: {
    background: "none", border: "1px solid #ddd", borderRadius: 8,
    padding: "9px 20px", fontSize: 13, cursor: "pointer", color: "#555",
  },
  delBtn: {
    background: "#A32D2D", color: "#fff", border: "none",
    borderRadius: 8, padding: "9px 20px", fontSize: 13, cursor: "pointer",
  },
};