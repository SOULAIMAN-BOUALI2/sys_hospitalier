import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { loginUser } from "../../services/authService";

export default function LoginPage() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    console.log(email,password)
    e.preventDefault();
    setError("");

    try {
      const data = await loginUser(email, password);

      // Sauvegarde dans localStorage
      localStorage.setItem("token", data.token);
      localStorage.setItem("role", data.role);
      localStorage.setItem("nom", data.nom);
      localStorage.setItem("prenom", data.prenom);
      localStorage.setItem("email", data.email);

      // Redirection selon le rôle
      if (data.role === "ADMIN") navigate("/admin/dashboard");
      else if (data.role === "MEDECIN") navigate("/medecin/dashboard");
      else if (data.role === "INFIRMIER") navigate("/infirmier/dashboard");

    } catch (err:unknown) {
      setError("Email ou mot de passe incorrect");
    }
  };

  return (
    <div className="min-h-screen bg-slate-950 flex items-center justify-center px-4">
      <div className="w-full max-w-md bg-slate-900 rounded-2xl shadow-2xl p-8">
        <h1 className="text-5xl font-bold text-white text-center mb-8">
          Connexion
        </h1>

        <form onSubmit={handleSubmit} className="flex flex-col gap-4">
          <input
            type="email"
            placeholder="Email"
            onChange={(e) => setEmail(e.target.value)}
            className="w-full px-4 py-3 rounded-lg bg-slate-800 border border-slate-700 text-white outline-none focus:ring-2 focus:ring-blue-500"
          />

          <input
            type="password"
            placeholder="Mot de passe"
            onChange={(e) => setPassword(e.target.value)}
            className="w-full px-4 py-3 rounded-lg bg-slate-800 border border-slate-700 text-white outline-none focus:ring-2 focus:ring-blue-500"
          />

          {error && (
            <p className="text-red-500 text-sm text-center">{error}</p>
          )}

          <button
            type="submit"
            className="w-full bg-blue-600 text-white py-3 rounded-lg font-semibold hover:bg-blue-700 transition"
          >
            Se connecter
          </button>
          <p className="text-center text-sm text-slate-500 mt-4">
            Contactez l'administrateur pour obtenir un accès
          </p>
        </form>
      </div>
    </div>
  );
}