import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import LoginPage from "./pages/auth/LoginPage";
import DashboardAdmin from "./pages/admin/DashboardAdmin";
import DashboardMedecin from "./pages/medecin/DashboardMedecin";
import DashboardInfirmier from "./pages/infirmier/DashboardInfirmier";
import type { ReactNode } from "react"; // ✅ import ajouté


function PrivateRoute({ children, role }: { children: ReactNode, role: string }) { // ✅ ReactNode au lieu de JSX.Element
  const token = localStorage.getItem("token");
  const userRole = localStorage.getItem("role");

  if (!token) return <Navigate to="/login" />;
  if (userRole !== role) return <Navigate to="/login" />;

  return <>{children}</>; // ✅ wrapped dans fragment
}

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<LoginPage />} />

        <Route path="/admin/dashboard" element={
          <PrivateRoute role="ADMIN">
            <DashboardAdmin />
          </PrivateRoute>
        } />

        <Route path="/medecin/dashboard" element={
          <PrivateRoute role="MEDECIN">
            <DashboardMedecin />
          </PrivateRoute>
        } />

        <Route path="/infirmier/dashboard" element={
          <PrivateRoute role="INFIRMIER">
            <DashboardInfirmier />
          </PrivateRoute>
        } />

        <Route path="*" element={<Navigate to="/login" />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;