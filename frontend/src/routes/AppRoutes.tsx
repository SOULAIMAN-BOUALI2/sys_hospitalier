import { Routes, Route, Navigate } from "react-router-dom";
import LoginPage from "../pages/auth/LoginPage";
import AddUserPage from "../pages/admin/AddUserPage";
import PrivateRoute from "./PrivateRoute";


export default function AppRoutes() {
  return (
    // Dans ton router
    <Routes>
      <Route path="/" element={<Navigate to="/login" />} />
      <Route path="/login" element={<LoginPage />} />
      <Route
        path="/add-user"
        element={
          <PrivateRoute>
            <AddUserPage />
          </PrivateRoute>
        }
      />
    </Routes>
  );
}