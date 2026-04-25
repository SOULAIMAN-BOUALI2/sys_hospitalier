import { useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";
import { getUsers } from "../../services/authService";

// ✅ mettre type en dehors du composant
type User = {
  nom: string;
  prenom: string;
  email: string;
  role: string;
};

export default function DashboardAdmin() {
  const navigate = useNavigate();
  const nom = localStorage.getItem("nom");
  const prenom = localStorage.getItem("prenom");

  const [users, setUsers] = useState<User[]>([]);
  const [selectedRole, setSelectedRole] = useState("medecin");

  const handleLogout = () => {
    localStorage.clear();
    navigate("/login");
  };

  const fetchUsers = async () => {
    try {
      const data = await getUsers();
      console.log("DATA =", data);
      setUsers(data);
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    fetchUsers();
  }, []);

  const filteredUsers = users.filter(u => u.role === selectedRole);
  console.log(filteredUsers)

  return (
    
    <div className="min-h-screen bg-slate-950 text-white p-8">
      <div className="max-w-4xl mx-auto">
        <div className="flex justify-between items-center mb-8">
          <h1 className="text-3xl font-bold">🏥 Dashboard Admin</h1>
          <button onClick={handleLogout}
            className="bg-red-600 px-4 py-2 rounded-lg hover:bg-red-700">
            Déconnexion
          </button>
        </div>

        <p className="text-slate-400 mb-8">Bienvenue, {prenom} {nom}</p>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          <div onClick={() => setSelectedRole("medecin")} className="bg-slate-800 rounded-xl p-6 cursor-pointer hover:bg-slate-700">
            <h2 className="text-xl font-semibold mb-2">👨‍⚕️ Médecins</h2>
            <p className="text-slate-400">Gérer les médecins</p>
          </div>
          <div onClick={() => setSelectedRole("INFIRMIER")} className="bg-slate-800 rounded-xl p-6 cursor-pointer hover:bg-slate-700">
            <h2 className="text-xl font-semibold mb-2">👩‍⚕️ Infirmiers</h2>
            <p className="text-slate-400">Gérer les infirmiers</p>
          </div>
          <div onClick={() => {
              console.log("TOKEN =", localStorage.getItem("token"));
              navigate("/add-user");
            }} className="bg-slate-800 rounded-xl p-6 cursor-pointer hover:bg-slate-700">
            <h2 className="text-xl font-semibold mb-2">➕ Créer un compte</h2>
            <p className="text-slate-400">Ajouter médecin ou infirmier</p>
          </div>
        </div>
        <div className="mt-8 bg-slate-800 rounded-xl p-4">
  <h2 className="text-xl mb-4">Liste</h2>

  <table className="w-full text-left">
    <thead>
      <tr className="border-b border-slate-600">
        <th className="p-2">Nom</th>
        <th className="p-2">Prénom</th>
        <th className="p-2">Email</th>
        <th className="p-2">Actions</th>
      </tr>
    </thead>

    <tbody>
      {filteredUsers.map((u, index) => (
        <tr key={index} className="border-b border-slate-700">
          <td className="p-2">{u.nom}</td>
          <td className="p-2">{u.prenom}</td>
          <td className="p-2">{u.email}</td>

          <td className="p-2 flex gap-2">
            <button className="bg-yellow-500 px-2 py-1 rounded">
              Modifier
            </button>
            <button className="bg-red-600 px-2 py-1 rounded">
              Supprimer
            </button>
          </td>
        </tr>
      ))}
    </tbody>
  </table>
</div>
      </div>
    </div>
  );
}