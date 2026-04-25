import { useState } from "react";
import axios from "axios";

export default function AddUserPage() {
    console.log("hhhhh");
  const [form, setForm] = useState({
    nom: "",
    prenom: "",
    email: "",
    motDePasse: "",
    role: "MEDECIN"
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
  setForm({ ...form, [e.target.name]: e.target.value });
};

const handleSubmit = async (e: React.FormEvent) => {
  e.preventDefault();

    console.log("BUTTON CLICKED"); // 👈 test

  const token = localStorage.getItem("token");
  

  await axios.post("http://localhost:8080/api/admin/create-user", form,
    {
    headers: {
      Authorization: `Bearer ${token}`
    }
  }
  );

  alert("User créé !");
};

  return (
    <form onSubmit={handleSubmit}>
      <input name="nom" onChange={handleChange} placeholder="Nom" />
      <input name="prenom" onChange={handleChange} placeholder="Prénom" />
      <input name="email" onChange={handleChange} placeholder="Email" />
      <input name="motDePasse" type="password" onChange={handleChange} placeholder="Password" />

      <select name="role" onChange={handleChange}>
        <option value="MEDECIN">Médecin</option>
        <option value="INFIRMIER">Infirmier</option>
      </select>

      <button type="submit">Créer</button>
    </form>
  );
}