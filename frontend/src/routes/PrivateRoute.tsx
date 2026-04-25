import React from 'react';
import { Navigate } from 'react-router-dom';
import type { ReactElement } from 'react';

export default function PrivateRoute({ children }: { children: ReactElement }) {
  const token = localStorage.getItem("token");
  console.log("--------Token:", token); // Pour déboguer
  
  return token ? children : <Navigate to="/login" />;
}