import { useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";
import { getUsers } from "../../services/authService";
import { createUser, updateUserPage, deleteUser } from "../../services/userService";
import { 
  Users, 
  UserPlus, 
  LogOut, 
  Edit3, 
  Trash2, 
  Stethoscope, 
  ClipboardList,
  Search
} from "lucide-react";

type User = {
  nom: string;
  prenom: string;
  email: string;
  role: string;
  matricule?: string;
  specialite?: string;
  service?: string;
  shift?: string;
};

export default function DashboardAdmin() {
  const navigate = useNavigate();
  const nom = localStorage.getItem("nom");
  const prenom = localStorage.getItem("prenom");

  const [form, setForm] = useState({
    nom: "", prenom: "", email: "", motDePasse: "",
    role: "MEDECIN", matricule: "", specialite: "", service: "", shift: ""
  });

  const [users, setUsers] = useState<User[]>([]);
  const [selectedRole, setSelectedRole] = useState("MEDECIN");

  const handleLogout = () => {
    localStorage.clear();
    navigate("/login");
  };

  const modifieUser = async (email: string) => {
    const response = await updateUserPage(email);
    navigate("/admin/update-user", { state: response.data });
  };

  const supprimerUser = async (email: string) => {
    if (window.confirm("Êtes-vous sûr de vouloir supprimer cet utilisateur ?")) {
      await deleteUser(email);
      fetchUsers(); // Recharger la liste
    }
  };

  const fetchUsers = async () => {
    try {
      const data = await getUsers();
      setUsers(data);
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => { fetchUsers(); }, []);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    await createUser(form);
    setSelectedRole("MEDECIN"); // Retour à la liste
    fetchUsers();
  };

  const filteredUsers = users.filter(u => u.role === selectedRole);

  return (
    <div className="min-h-screen bg-slate-950 text-slate-100 antialiased pb-12">
      {/* NAVBAR */}
      <nav className="border-b border-slate-800 bg-slate-900/50 backdrop-blur-md sticky top-0 z-10">
        <div className="max-w-6xl mx-auto px-6 py-4 flex items-center justify-between">
          <div className="flex items-center gap-2">
            <div className="bg-blue-600 p-2 rounded-lg">
              <ClipboardList size={24} className="text-white" />
            </div>
            <h1 className="text-xl font-bold tracking-tight">Hospital<span className="text-blue-500">Admin</span></h1>
          </div>
          
          <div className="flex items-center gap-6">
            <div className="text-right hidden sm:block">
              <p className="text-sm font-medium">{prenom} {nom}</p>
              <p className="text-[10px] text-slate-500 uppercase tracking-widest">Administrateur Principal</p>
            </div>
            <button 
              onClick={handleLogout}
              className="flex items-center gap-2 bg-slate-800 hover:bg-red-950 hover:text-red-400 border border-slate-700 px-4 py-2 rounded-lg transition-all text-sm font-semibold"
            >
              <LogOut size={16} />
              Déconnexion
            </button>
          </div>
        </div>
      </nav>

      <div className="max-w-6xl mx-auto px-6 pt-8">
        {/* CARDS NAVIGATION */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-10">
          <button 
            onClick={() => setSelectedRole("MEDECIN")}
            className={`group p-6 rounded-2xl border transition-all text-left ${selectedRole === "MEDECIN" ? 'bg-blue-600 border-blue-400 shadow-lg shadow-blue-900/20' : 'bg-slate-900 border-slate-800 hover:border-slate-600'}`}
          >
            <Stethoscope className={`mb-4 ${selectedRole === "MEDECIN" ? 'text-white' : 'text-blue-400'}`} size={32} />
            <h2 className="text-xl font-bold mb-1">Médecins</h2>
            <p className={`${selectedRole === "MEDECIN" ? 'text-blue-100' : 'text-slate-400'} text-sm`}>Gérer le corps médical</p>
          </button>

          <button 
            onClick={() => setSelectedRole("INFIRMIER")}
            className={`group p-6 rounded-2xl border transition-all text-left ${selectedRole === "INFIRMIER" ? 'bg-indigo-600 border-indigo-400 shadow-lg shadow-indigo-900/20' : 'bg-slate-900 border-slate-800 hover:border-slate-600'}`}
          >
            <Users className={`mb-4 ${selectedRole === "INFIRMIER" ? 'text-white' : 'text-indigo-400'}`} size={32} />
            <h2 className="text-xl font-bold mb-1">Infirmiers</h2>
            <p className={`${selectedRole === "INFIRMIER" ? 'text-indigo-100' : 'text-slate-400'} text-sm`}>Gérer le personnel soignant</p>
          </button>

          <button 
            onClick={() => setSelectedRole("")}
            className={`group p-6 rounded-2xl border transition-all text-left ${selectedRole === "" ? 'bg-emerald-600 border-emerald-400 shadow-lg shadow-emerald-900/20' : 'bg-slate-900 border-slate-800 hover:border-slate-600'}`}
          >
            <UserPlus className={`mb-4 ${selectedRole === "" ? 'text-white' : 'text-emerald-400'}`} size={32} />
            <h2 className="text-xl font-bold mb-1">Recrutement</h2>
            <p className={`${selectedRole === "" ? 'text-emerald-100' : 'text-slate-400'} text-sm`}>Ajouter un nouveau compte</p>
          </button>
        </div>

        {/* CONTENT AREA */}
        <div className="bg-slate-900 border border-slate-800 rounded-2xl shadow-xl overflow-hidden">
          
          {selectedRole !== "" ? (
            <div className="p-0">
              <div className="p-6 border-b border-slate-800 flex justify-between items-center bg-slate-900/50">
                <h3 className="text-lg font-bold flex items-center gap-2">
                  <Search size={18} className="text-slate-500" />
                  Liste des {selectedRole.toLowerCase()}s
                </h3>
                <span className="bg-slate-800 text-slate-400 text-xs font-bold px-3 py-1 rounded-full border border-slate-700">
                  {filteredUsers.length} au total
                </span>
              </div>
              <div className="overflow-x-auto">
                <table className="w-full text-left border-collapse">
                  <thead>
                    <tr className="text-slate-500 text-xs uppercase tracking-widest bg-slate-950/50">
                      <th className="px-6 py-4 font-semibold">Utilisateur</th>
                      <th className="px-6 py-4 font-semibold">Email</th>
                      <th className="px-6 py-4 font-semibold text-right">Actions</th>
                    </tr>
                  </thead>
                  <tbody className="divide-y divide-slate-800">
                    {filteredUsers.map((u, index) => (
                      <tr key={index} className="hover:bg-slate-800/30 transition-colors group">
                        <td className="px-6 py-4">
                          <div className="flex items-center gap-3">
                            <div className="h-10 w-10 rounded-full bg-gradient-to-br from-slate-700 to-slate-800 flex items-center justify-center text-blue-400 font-bold border border-slate-700">
                              {u.nom[0]}{u.prenom[0]}
                            </div>
                            <div>
                              <p className="font-semibold text-slate-200">{u.nom} {u.prenom}</p>
                              <p className="text-xs text-slate-500">{u.role}</p>
                            </div>
                          </div>
                        </td>
                        <td className="px-6 py-4 text-slate-400 font-mono text-sm">{u.email}</td>
                        <td className="px-6 py-4 text-right">
                          <div className="flex gap-2 justify-end">
                            <button 
                              onClick={() => modifieUser(u.email)}
                              className="p-2 hover:bg-blue-500/10 text-slate-400 hover:text-blue-400 rounded-lg transition-all"
                              title="Modifier"
                            >
                              <Edit3 size={18} />
                            </button>
                            <button 
                              onClick={() => supprimerUser(u.email)}
                              className="p-2 hover:bg-red-500/10 text-slate-400 hover:text-red-500 rounded-lg transition-all"
                              title="Supprimer"
                            >
                              <Trash2 size={18} />
                            </button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          ) : (
            /* FORMULAIRE DE CRÉATION */
            <div className="p-8 max-w-2xl mx-auto">
              <div className="text-center mb-8">
                <h3 className="text-2xl font-bold text-white">Nouveau Collaborateur</h3>
                <p className="text-slate-400 mt-1">Remplissez les informations pour créer un accès.</p>
              </div>
              <form onSubmit={handleSubmit} className="space-y-5">
                <div className="grid grid-cols-2 gap-4">
                  <div className="space-y-1">
                    <label className="text-xs font-semibold text-slate-500 uppercase ml-1">Nom</label>
                    <input name="nom" onChange={handleChange} className="w-full bg-slate-800 border border-slate-700 rounded-xl px-4 py-3 focus:ring-2 focus:ring-emerald-500/50 outline-none transition-all" placeholder="ex: Bouali" />
                  </div>
                  <div className="space-y-1">
                    <label className="text-xs font-semibold text-slate-500 uppercase ml-1">Prénom</label>
                    <input name="prenom" onChange={handleChange} className="w-full bg-slate-800 border border-slate-700 rounded-xl px-4 py-3 focus:ring-2 focus:ring-emerald-500/50 outline-none transition-all" placeholder="ex: Soulaiman" />
                  </div>
                </div>

                <div className="space-y-1">
                  <label className="text-xs font-semibold text-slate-500 uppercase ml-1">Email Professionnel</label>
                  <input name="email" type="email" onChange={handleChange} className="w-full bg-slate-800 border border-slate-700 rounded-xl px-4 py-3 focus:ring-2 focus:ring-emerald-500/50 outline-none transition-all" placeholder="nom@hopital.ma" />
                </div>

                <div className="grid grid-cols-2 gap-4">
                  <div className="space-y-1">
                    <label className="text-xs font-semibold text-slate-500 uppercase ml-1">Mot de passe</label>
                    <input name="motDePasse" type="password" onChange={handleChange} className="w-full bg-slate-800 border border-slate-700 rounded-xl px-4 py-3 focus:ring-2 focus:ring-emerald-500/50 outline-none transition-all" placeholder="••••••••" />
                  </div>
                  <div className="space-y-1">
                    <label className="text-xs font-semibold text-slate-500 uppercase ml-1">Rôle</label>
                    <select name="role" onChange={handleChange} className="w-full bg-slate-800 border border-slate-700 rounded-xl px-4 py-3 outline-none focus:ring-2 focus:ring-emerald-500/50">
                      <option value="MEDECIN">Médecin</option>
                      <option value="INFIRMIER">Infirmier</option>
                    </select>
                  </div>
                </div>

                <div className="p-4 bg-slate-950/50 border border-slate-800 rounded-xl space-y-4">
                  {form.role === "MEDECIN" ? (
                    <div className="grid grid-cols-2 gap-4">
                      <input name="matricule" onChange={handleChange} placeholder="Matricule" className="bg-slate-900 border border-slate-700 rounded-lg px-4 py-2 text-sm outline-none focus:border-emerald-500" />
                      <input name="specialite" onChange={handleChange} placeholder="Spécialité" className="bg-slate-900 border border-slate-700 rounded-lg px-4 py-2 text-sm outline-none focus:border-emerald-500" />
                    </div>
                  ) : (
                    <div className="grid grid-cols-3 gap-3">
                      <input name="matricule" onChange={handleChange} placeholder="Matricule" className="bg-slate-900 border border-slate-700 rounded-lg px-3 py-2 text-sm outline-none" />
                      <input name="service" onChange={handleChange} placeholder="Service" className="bg-slate-900 border border-slate-700 rounded-lg px-3 py-2 text-sm outline-none" />
                      <input name="shift" onChange={handleChange} placeholder="Shift" className="bg-slate-900 border border-slate-700 rounded-lg px-3 py-2 text-sm outline-none" />
                    </div>
                  )}
                </div>

                <button type="submit" className="w-full bg-emerald-600 hover:bg-emerald-500 text-white font-bold py-4 rounded-xl shadow-lg shadow-emerald-900/20 transition-all active:scale-[0.98]">
                  Créer le compte
                </button>
              </form>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}